package ru.iprustam.trainee.simbirchat.util.role;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;

public class UserUtils {
    public static ChatUser getCurrentPrincipal() {
        return (ChatUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
