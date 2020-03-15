package ru.iprustam.trainee.simbirchat.service.wscom.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.iprustam.trainee.simbirchat.service.wscom.SendMessageHandler;

/**
 * Конфигурирование паттерна Chain of responsibilities
 */
@Configuration
public class ConfigureHandlers {
    @Bean
    public MessageHandler configuredMessageHandler() {
        MessageHandler handler = sendMessageHandler();
        //handler.setNext(...).setNext(...);
        return handler;
    }

    @Bean
    public MessageHandler sendMessageHandler() {
        return new SendMessageHandler();
    }
}
