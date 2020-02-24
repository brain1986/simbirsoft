package ru.iprustam.trainee.simbirchat.room;

public class RoomFactory {

    public static Room createPublicRoom() {
        PublicRoom room = new PublicRoom();
        return room;
    }

    public static Room createPrivateRoom() {
        PrivateRoom room = new PrivateRoom();
        return room;
    }

    public static Room createBotRoom() {
        BotRoom room = new BotRoom();
        return room;
    }
}
