package ru.iprustam.trainee.simbirchat.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "role")
public class ChatUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short roleId;
    private String roleName;

    @OneToMany(mappedBy="role")
    private Set<ChatUser> users;
}
