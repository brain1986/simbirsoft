package ru.iprustam.trainee.simbirchat.entity;

import org.hibernate.annotations.ColumnDefault;
import ru.iprustam.trainee.simbirchat.util.role.ChatAuthorities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "role")
public class ChatUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short roleId;
    private String roleName;
    @ColumnDefault("0")
    private Short authorities;

    @OneToMany(mappedBy="role")
    private Set<ChatUser> users;

    public boolean hasAuthority(ChatAuthorities authority) {
        int bitOffset = authority.ordinal();
        return ((authorities >> bitOffset) & 0x1) == 0x1;
    }

    public void setUsers(Set<ChatUser> users) {
        this.users = users;
    }

    public void setRoleId(Short roleId) {
        this.roleId = roleId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Short getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public Set<ChatUser> getUsers() {
        return users;
    }


}
