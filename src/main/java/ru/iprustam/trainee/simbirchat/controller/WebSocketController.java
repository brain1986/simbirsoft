package ru.iprustam.trainee.simbirchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import ru.iprustam.trainee.simbirchat.entity.ChatMessage;
import ru.iprustam.trainee.simbirchat.service.WsChatService;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final WsChatService wsChatService;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate, WsChatService wsChatService) {
        this.messagingTemplate = messagingTemplate;
        this.wsChatService = wsChatService;
    }

    @SubscribeMapping("/queue/rooms-common-events")
    public void roomsCommonEventsSubscribe() {
        wsChatService.subscribe();
    }

    //todo: запретить subscribe к /topic/room-concrete/{roomId}, если юзер не добавлен к данной комнате

    @SubscribeMapping("/room-concrete/{roomId}")
    public void roomConcreteSubscribe(@DestinationVariable Long roomId) {
        wsChatService.messagesLoad(roomId);
    }

    @MessageMapping("/message-send/{roomId}")
    public void messageSend(ChatMessage message, @DestinationVariable Long roomId) {
        wsChatService.messageSend(message, roomId);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
