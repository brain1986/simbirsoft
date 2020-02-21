package ru.iprustam.trainee.simbirchat.user;

import ru.iprustam.trainee.simbirchat.handlers.Handler;
import ru.iprustam.trainee.simbirchat.handlers.messagehandlers.*;
import ru.iprustam.trainee.simbirchat.handlers.roomhandlers.*;
import ru.iprustam.trainee.simbirchat.handlers.userhandlers.*;

public class UserFactory {
    public static User createAdmin() {
        AdminUser user = new AdminUser();
        Handler handlerChain = new LockUser()
                .setNext(new UnlockUser())
                .setNext(new SetModerator())
                .setNext(new DeleteModerator())
                .setNext(new DeleteMessage())
                .setNext(new SendMessage())
                .setNext(new ReceiveMessage())
                .setNext(new DeleteMessage())
                .setNext(new CreateOrdinaryRoom())
                .setNext(new CreatePrivateRoom())
                .setNext(new AddRoomUser())
                .setNext(new DeleteRoomUser())
                .setNext(new RenameRoom());

        user.setUserActions(handlerChain);
        return user;
    }

    public static User createBot() {
        BotUser user = new BotUser();
        Handler handlerChain = null;

        user.setUserActions(handlerChain);
        return user;
    }

    public static User createModerator() {
        ModeratorUser user = new ModeratorUser();
        Handler handlerChain = new LockUser()
                .setNext(new UnlockUser())
                .setNext(new SetModerator())
                .setNext(new SendMessage())
                .setNext(new ReceiveMessage())
                .setNext(new DeleteMessage())
                .setNext(new CreateOrdinaryRoom())
                .setNext(new CreatePrivateRoom())
                .setNext(new AddRoomUser());

        user.setUserActions(handlerChain);
        return user;
    }

    public static User createOrdinaryUser() {
        OrdinaryUser user = new OrdinaryUser();
        Handler handlerChain = new SendMessage()
                .setNext(new ReceiveMessage())
                .setNext(new CreateOrdinaryRoom())
                .setNext(new CreatePrivateRoom())
                .setNext(new AddRoomUser());

        user.setUserActions(handlerChain);
        return user;
    }

    public static User createUnknownUser() {
        UnknownUser user = new UnknownUser();
        Handler handlerChain = null;

        user.setUserActions(handlerChain);
        return user;
    }

}
