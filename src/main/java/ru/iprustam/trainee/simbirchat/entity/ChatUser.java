package ru.iprustam.trainee.simbirchat.entity;

import ru.iprustam.trainee.simbirchat.handler.Handler;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "usr")
public class ChatUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String password;

    @ManyToMany(mappedBy = "users")
    private Set<ChatRoom> rooms;

    @OneToMany(mappedBy="chatUser")
    private Set<ChatMessage> messages;

    @ManyToOne
    @JoinColumn(name="role_id", nullable=false)
    private ChatUserRole role;

    @Transient
    private Handler userActions;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Handler getUserActions() {
        return userActions;
    }

    public void setUserActions(Handler userActions) {
        this.userActions = userActions;
    }
}
