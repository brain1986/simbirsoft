package ru.iprustam.trainee.simbirchat.service.wscom.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.session.SessionRegistry;
import ru.iprustam.trainee.simbirchat.dto.DtoMapper;
import ru.iprustam.trainee.simbirchat.dto.DtoPacket;
import ru.iprustam.trainee.simbirchat.entity.ChatMessage;
import ru.iprustam.trainee.simbirchat.entity.ChatRoom;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;
import ru.iprustam.trainee.simbirchat.service.MessageService;
import ru.iprustam.trainee.simbirchat.service.RoomService;
import ru.iprustam.trainee.simbirchat.service.UserService;
import ru.iprustam.trainee.simbirchat.util.role.UserUtils;
import ru.iprustam.trainee.simbirchat.util.wsevent.WsEvent;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Класс реализует паттерн Chain of responsibilities для выполнения команд,
 * полученных от пользователя
 */
public abstract class BaseMessageHandler implements MessageHandler {

    protected SimpMessagingTemplate messagingTemplate;
    protected SessionRegistry sessionRegistry;
    protected UserService userService;
    protected RoomService roomService;
    protected MessageService messageService;
    protected DtoMapper dtoMapper;
    private MessageHandler nextHandler;

    abstract protected boolean canHandle(ChatCommand chatCommand);

    abstract protected void doHandle(ChatCommand chatCommand) throws Exception;

    abstract protected String help();

    @Override
    public final MessageHandler setNext(MessageHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    /**
     * Поиск обработчика, готового обработать команду
     *
     * @param chatCommand
     */
    @Override
    public final void handle(ChatCommand chatCommand) {
        try {
            if (canHandle(chatCommand)) {
                prepareMessageForEcho(chatCommand);
                doHandle(chatCommand);
                if (chatCommand.getCommand().equals("msg"))
                    sendEchoToRoom(chatCommand);
                else
                    sendEchoToUser(chatCommand);
            } else {
                if (nextHandler != null)
                    nextHandler.handle(chatCommand);
                else
                    throw new Exception("Can't handle the request");
            }
        } catch (Exception e) {
            sendCustomMessageToUser(e.getMessage(), chatCommand);
        }
    }

    /**
     * Каждый обработчик выдаст информацию о себе и результат будет отправлен
     * в чат
     */
    @Override
    public void helpCommandsToWs(ChatCommand chatCommand) {
        StringBuilder helpCommands = new StringBuilder();
        helpCommands = collectHelpCommands(helpCommands);
        sendCustomMessageToUser(helpCommands.toString(), chatCommand);
    }

    public StringBuilder collectHelpCommands(StringBuilder helpCommands) {
        helpCommands
                .append("<hr />")
                .append(help());
        if (nextHandler != null) {
            ((BaseMessageHandler) nextHandler).collectHelpCommands(helpCommands);
        }
        return helpCommands;
    }

    /**
     * Выполняет подготовку объекта сообщения перед отправкой обратно пользователям комнаты
     *
     * @param chatCommand
     */
    protected void prepareMessageForEcho(ChatCommand chatCommand) {
        ChatUser chatUser = UserUtils.getCurrentPrincipal();
        ChatMessage chatMessage = chatCommand.getChatMessage();
        if (chatCommand.getCommand().equals("msg"))
            chatMessage.setMessage(chatCommand.getParam("m"));
        chatMessage.setChatUser(chatUser);
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(chatCommand.getRoomId());
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setMessageTime(ZonedDateTime.now(ZoneId.systemDefault()));
    }

    private void sendCustomMessageToUser(String message, ChatCommand chatCommand) {
        prepareMessageForEcho(chatCommand);
        ChatMessage chatMessage = chatCommand.getChatMessage();
        chatMessage.setMessage(chatMessage.getMessage() + "<br />" + message);
        sendEchoToUser(chatCommand);
    }

    /**
     * Отправляет сообщение всем пользователям комнаты (для обычных текстовых сообщений)
     *
     * @param chatCommand
     */
    protected void sendEchoToRoom(ChatCommand chatCommand) {
        DtoPacket packet = new DtoPacket(WsEvent.NEW_MESSAGE, dtoMapper.msgToDto(chatCommand.getChatMessage()));
        messagingTemplate.convertAndSend(
                "/topic/room-concrete/" + chatCommand.getRoomId(), packet);
    }

    /**
     * Отправляет сообщение обратно только текущему пользователю (пользователь отправлял в чат системную команду)
     *
     * @param chatCommand
     */
    protected void sendEchoToUser(ChatCommand chatCommand) {
        ChatUser chatUser = UserUtils.getCurrentPrincipal();
        DtoPacket packet = new DtoPacket(WsEvent.SYSTEM_COMMAND, dtoMapper.msgToDto(chatCommand.getChatMessage()));
        messagingTemplate.convertAndSend(
                "/user/" + chatUser.getUsername() + "/queue/rooms-common-events", packet);
    }

    @Autowired
    public final void setMessagingTemplate(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Autowired
    public void setDtoMapper(DtoMapper dtoMapper) {
        this.dtoMapper = dtoMapper;
    }

    @Autowired
    public final void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public final void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    @Autowired
    public final void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public final void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }
}
