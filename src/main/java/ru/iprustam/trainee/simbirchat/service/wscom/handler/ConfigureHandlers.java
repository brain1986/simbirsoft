package ru.iprustam.trainee.simbirchat.service.wscom.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.iprustam.trainee.simbirchat.service.wscom.*;

/**
 * Конфигурирование паттерна Chain of responsibilities
 */
@Configuration
public class ConfigureHandlers {
    @Bean
    public MessageHandler configuredMessageHandler() {
        MessageHandler handler = roomCreateHandler();
        handler
                .setNext(roomRemoveHandler())
                .setNext(roomRenameHandler())
                .setNext(roomConnectHandler())
                .setNext(roomDisconnectHandler())
                .setNext(userRenameHandler())
                .setNext(userBanHandler())
                .setNext(userModeratorHandler())
                .setNext(yBotFindHandler())
                .setNext(yBotHelpHandler())
                .setNext(messageSendHandler());

        return handler;
    }

    @Bean
    public MessageHandler messageSendHandler() {
        return new MessageSendHandler();
    }

    @Bean
    public MessageHandler roomCreateHandler() {
        return new RoomCreateHandler();
    }

    @Bean
    public MessageHandler roomRemoveHandler() {
        return new RoomRemoveHandler();
    }

    @Bean
    public MessageHandler roomRenameHandler() {
        return new RoomRenameHandler();
    }

    @Bean
    public MessageHandler roomConnectHandler() {
        return new RoomConnectHandler();
    }

    @Bean
    public MessageHandler roomDisconnectHandler() {
        return new RoomDisconnectHandler();
    }

    @Bean
    public MessageHandler userRenameHandler() {
        return new UserRenameHandler();
    }

    @Bean
    public MessageHandler userBanHandler() {
        return new UserBanHandler();
    }

    @Bean
    public MessageHandler userModeratorHandler() {
        return new UserModeratorHandler();
    }

    @Bean
    public MessageHandler yBotFindHandler() {
        return new YBotFindHandler();
    }

    @Bean
    public MessageHandler yBotHelpHandler() {
        return new YBotHelpHandler();
    }

}
