package ru.iprustam.trainee.simbirchat.util.user;

import ru.iprustam.trainee.simbirchat.entity.ChatUser;


public class UserFactory {
    public static ChatUser createAdmin() {
        ChatUser user = new ChatUser();
//        Handler handlerChain = new LockUser()
//                .setNext(new UnlockUser())
//                .setNext(new SetModerator())
//                .setNext(new DeleteModerator())
//                .setNext(new DeleteMessage())
//                .setNext(new SendMessage())
//                .setNext(new ReceiveMessage())
//                .setNext(new DeleteMessage())
//                .setNext(new CreateOrdinaryRoom())
//                .setNext(new CreatePrivateRoom())
//                .setNext(new AddRoomUser())
//                .setNext(new DeleteRoomUser())
//                .setNext(new RenameRoom());
//
//        user.setUserActions(handlerChain);
        return user;
    }

    public static ChatUser createBot() {
        ChatUser user = new ChatUser();
//        Handler handlerChain = null;
//
//        user.setUserActions(handlerChain);
        return user;
    }

    public static ChatUser createModerator() {
        ChatUser user = new ChatUser();
//        Handler handlerChain = new LockUser()
//                .setNext(new UnlockUser())
//                .setNext(new SetModerator())
//                .setNext(new SendMessage())
//                .setNext(new ReceiveMessage())
//                .setNext(new DeleteMessage())
//                .setNext(new CreateOrdinaryRoom())
//                .setNext(new CreatePrivateRoom())
//                .setNext(new AddRoomUser());
//
//        user.setUserActions(handlerChain);
        return user;
    }

    public static ChatUser createOrdinaryUser() {
        ChatUser user = new ChatUser();
//        Handler handlerChain = new SendMessage()
//                .setNext(new ReceiveMessage())
//                .setNext(new CreateOrdinaryRoom())
//                .setNext(new CreatePrivateRoom())
//                .setNext(new AddRoomUser());
//
//        user.setUserActions(handlerChain);
        return user;
    }

    public static ChatUser createUnknownUser() {
        ChatUser user = new ChatUser();
//        Handler handlerChain = null;
//
//        user.setUserActions(handlerChain);
        return user;
    }

}
