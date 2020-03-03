package ru.iprustam.trainee.simbirchat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import static org.springframework.messaging.simp.SimpMessageType.MESSAGE;
import static org.springframework.messaging.simp.SimpMessageType.SUBSCRIBE;

@Configuration
public class WebSocketSecurityConfig
            extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpDestMatchers("/app/**").authenticated()
                .simpMessageDestMatchers("/queue/rooms-common-events", "/topic/room-concrete/**").denyAll()
                .simpSubscribeDestMatchers("/user/**", "/topic/**").authenticated()
                .simpTypeMatchers(MESSAGE, SUBSCRIBE).denyAll()
                .nullDestMatcher().authenticated()
                .anyMessage().denyAll();
    }
}