package ru.iprustam.trainee.simbirchat.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import ru.iprustam.trainee.simbirchat.util.role.ChatAuthorities;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usr")
public class ChatUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String password;
    private boolean blocked;

    @ManyToMany(mappedBy = "users")
    private Set<ChatRoom> rooms;

    @OneToMany(mappedBy = "chatUser", cascade = CascadeType.REMOVE)
    private Set<ChatMessage> messages;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private ChatUserRole role;

    @OneToMany(mappedBy = "owner")
    private Set<ChatRoom> roomsWhichOwn;

    @PreRemove
    private void removeUser() {
        for (ChatRoom r : rooms) {
            r.getUsers().remove(this);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Add role
        StringBuilder rolesAndAuthorities = new StringBuilder(role.getRoleName() + ",");
        // Add authorities
        ChatAuthorities[] allAuthorities = ChatAuthorities.values();
        Arrays.stream(allAuthorities).filter(a -> role.hasAuthority(a)).forEach(a -> rolesAndAuthorities.append(a + ","));

        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(
                rolesAndAuthorities.toString());

        return authorities;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public Set<ChatRoom> getRooms() {
        return rooms;
    }

    public void setRooms(Set<ChatRoom> rooms) {
        this.rooms = rooms;
    }

    public Set<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(Set<ChatMessage> messages) {
        this.messages = messages;
    }

    public ChatUserRole getRole() {
        return role;
    }

    public void setRole(ChatUserRole role) {
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
