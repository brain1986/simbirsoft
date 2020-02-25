package ru.iprustam.trainee.simbirchat.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.util.HtmlUtils;
import ru.iprustam.trainee.simbirchat.repository.ChatMessageRepository;
import ru.iprustam.trainee.simbirchat.repository.ChatUserRepository;
import ru.iprustam.trainee.simbirchat.repository.ChatRoomRepository;
import ru.iprustam.trainee.simbirchat.websocket.model.WsIngoing;
import ru.iprustam.trainee.simbirchat.websocket.model.WsOutgoingNewMessage;
import ru.iprustam.trainee.simbirchat.websocket.model.WsOutgoingUserList;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class WebSocketController {
    //Temporal
    ArrayList<String> activeUsers = new ArrayList<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private ChatUserRepository chatUserRepository;

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        GenericMessage message = (GenericMessage) event.getMessage();
        //String simpSessionId = (String) message.getHeaders().get("simpSessionId");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = headerAccessor.getFirstNativeHeader("username");
        activeUsers.add(username);

        messagingTemplate.convertAndSend("/queue/chat-updates", new WsOutgoingUserList(activeUsers));
    }

    @SendTo("/queue/chat-updates")
    @MessageMapping("/send-message")
    public WsOutgoingNewMessage sendMessage(WsIngoing message, @Header("simpSessionId") String sessionId) {
        WsOutgoingNewMessage outgoingMessage = new WsOutgoingNewMessage();
        outgoingMessage.setUser(message.getUsername());
        outgoingMessage.setTime(ZonedDateTime.now(ZoneId.systemDefault()).toString());
        outgoingMessage.setRoomId(1);
        outgoingMessage.setMessage(HtmlUtils.htmlEscape(message.getMessage()));

        return outgoingMessage;
    }
}
