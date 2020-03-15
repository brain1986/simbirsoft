package ru.iprustam.trainee.simbirchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import ru.iprustam.trainee.simbirchat.entity.ChatMessage;
import ru.iprustam.trainee.simbirchat.service.WsChatService;

@Controller
public class WebSocketController {

    private final WsChatService wsChatService;

    @Autowired
    public WebSocketController(WsChatService wsChatService) {
        this.wsChatService = wsChatService;
    }

    @SubscribeMapping("/queue/rooms-common-events")
    public void roomsCommonEventsSubscribe() {
        wsChatService.subscribe();
    }

    @SubscribeMapping("/room-concrete/{roomId}")
    public void roomConcreteSubscribe(@DestinationVariable Long roomId) {
        wsChatService.roomSubscribe(roomId);
    }

    @MessageMapping("/message-send/{roomId}")
    public void messageSend(ChatMessage message, @DestinationVariable Long roomId) throws Exception {
        wsChatService.incomeMessageHandle(message, roomId);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
