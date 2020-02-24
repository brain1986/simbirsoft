package ru.iprustam.trainee.simbirchat.user;

import ru.iprustam.trainee.simbirchat.handlers.Handler;
import ru.iprustam.trainee.simbirchat.handlers.messagehandlers.*;
import ru.iprustam.trainee.simbirchat.handlers.roomhandlers.*;
import ru.iprustam.trainee.simbirchat.handlers.userhandlers.*;

public class ChatUserFactory {
    public static ChatUser createAdmin() {
        AdminChatUser user = new AdminChatUser();
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

    public static ChatUser createBot() {
        BotChatUser user = new BotChatUser();
        Handler handlerChain = null;

        user.setUserActions(handlerChain);
        return user;
    }

    public static ChatUser createModerator() {
        ModeratorChatUser user = new ModeratorChatUser();
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

    public static ChatUser createOrdinaryUser() {
        OrdinaryChatUser user = new OrdinaryChatUser();
        Handler handlerChain = new SendMessage()
                .setNext(new ReceiveMessage())
                .setNext(new CreateOrdinaryRoom())
                .setNext(new CreatePrivateRoom())
                .setNext(new AddRoomUser());

        user.setUserActions(handlerChain);
        return user;
    }

    public static ChatUser createUnknownUser() {
        UnknownChatUser user = new UnknownChatUser();
        Handler handlerChain = null;

        user.setUserActions(handlerChain);
        return user;
    }

}
