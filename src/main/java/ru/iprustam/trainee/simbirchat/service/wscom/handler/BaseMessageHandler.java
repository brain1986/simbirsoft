package ru.iprustam.trainee.simbirchat.service.wscom.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.iprustam.trainee.simbirchat.dto.DtoTransport;
import ru.iprustam.trainee.simbirchat.entity.ChatMessage;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;
import ru.iprustam.trainee.simbirchat.service.MessageService;
import ru.iprustam.trainee.simbirchat.service.RoomService;
import ru.iprustam.trainee.simbirchat.service.UserService;
import ru.iprustam.trainee.simbirchat.util.role.UserUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Класс реализует паттерн Chain of responsibilities для выполнения команд,
 * полученных от пользователя
 */
public abstract class BaseMessageHandler implements MessageHandler {

    protected SimpMessagingTemplate messagingTemplate;
    protected UserService userService;
    protected DtoTransport dtoTransport;
    protected RoomService roomService;
    protected MessageService messageService;
    private MessageHandler nextHandler;

    abstract protected boolean canHandle(ChatCommand chatCommand);
    abstract protected void doHandle(ChatCommand chatCommand, Long roomId);

    @Override
    public final MessageHandler setNext(MessageHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    @Override
    public final void handle(ChatCommand chatCommand, Long roomId) throws Exception {
        if (canHandle(chatCommand)) {
            prepareMessageForEcho(chatCommand, roomId);
            doHandle(chatCommand, roomId);
            sendEcho(chatCommand, roomId);
        } else {
            if (nextHandler != null) {
                nextHandler.handle(chatCommand, roomId);
            } else {
                String error = "Can't handle the request";
                sendErrorEcho(error, chatCommand, roomId);
                throw new Exception(error);
            }
        }
    }

    private void sendEcho(ChatCommand chatCommand, Long roomId) {
        if(chatCommand.getCommand().equals("msg"))
            sendEchoToRoom(chatCommand, roomId);
        else
            sendEchoUser(chatCommand);
    }

    private void sendErrorEcho(String error, ChatCommand chatCommand, Long roomId) {
        prepareMessageForEcho(chatCommand, roomId);
        ChatMessage chatMessage = chatCommand.getChatMessage();
        chatMessage.setMessage(chatMessage.getMessage() + "<br />" + error);
        sendEchoUser(chatCommand);
    }

    /**
     * Выполняет подготовку объекта сообщения перед отправкой обратно пользователям комнаты
     *
     * @param chatCommand
     * @param roomId
     */
    protected void prepareMessageForEcho(ChatCommand chatCommand, Long roomId) {
        ChatUser chatUser = UserUtils.getCurrentPrincipal();
        ChatMessage chatMessage = chatCommand.getChatMessage();
        if (chatCommand.getCommand().equals("msg"))
            chatMessage.setMessage(chatCommand.getParam("m"));
        chatMessage.setChatUser(chatUser);
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(roomId);
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setMessageTime(ZonedDateTime.now(ZoneId.systemDefault()));
    }

    /**
     * Отправляет сообщение всем пользователям комнаты (для обычных текстовых сообщений)
     *
     * @param chatCommand
     * @param roomId
     */
    protected void sendEchoToRoom(ChatCommand chatCommand, Long roomId) {
        messagingTemplate.convertAndSend(
                "/topic/room-concrete/" + roomId,
                dtoTransport.chatMessageToDto("new_message", chatCommand.getChatMessage()));
    }

    /**
     * Отправляет сообщение обратно только текущему пользователю (пользователь отправлял в чат системную команду)
     *
     * @param chatCommand
     */
    protected void sendEchoUser(ChatCommand chatCommand) {
        ChatUser chatUser = UserUtils.getCurrentPrincipal();
        messagingTemplate.convertAndSend(
                "/user/" + chatUser.getUsername() + "/queue/rooms-common-events",
                dtoTransport.chatMessageToDto("system_command", chatCommand.getChatMessage()));
    }

    @Autowired
    public final void setMessagingTemplate(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Autowired
    public final void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public final void setDtoTransport(DtoTransport dtoTransport) {
        this.dtoTransport = dtoTransport;
    }

    @Autowired
    public final void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @Autowired
    public final void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
