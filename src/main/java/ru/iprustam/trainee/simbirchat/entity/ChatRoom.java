package ru.iprustam.trainee.simbirchat.entity;

import ru.iprustam.trainee.simbirchat.room.RoomTypes;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "room")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    private RoomTypes roomType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "room_user",
            joinColumns = { @JoinColumn(name = "room_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private Set<ChatUser> users;

    @OneToMany(mappedBy="chatRoom")
    private Set<ChatMessage> messages;
}
