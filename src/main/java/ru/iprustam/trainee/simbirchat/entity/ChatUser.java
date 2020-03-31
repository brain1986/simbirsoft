package ru.iprustam.trainee.simbirchat.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import ru.iprustam.trainee.simbirchat.util.role.ChatAuthority;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usr")
public class ChatUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String password;
    private ZonedDateTime globalBlockUntil;
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private ChatUserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Add role
        StringBuilder rolesAndAuthorities = new StringBuilder(role.getRoleName() + ",");
        // Add authorities
        ChatAuthority[] allAuthorities = ChatAuthority.values();
        Arrays.stream(allAuthorities)
                .filter(a -> (role.getAuthorities() >> a.ordinal() & 0x1) == 0x1)
                .forEach(a -> rolesAndAuthorities.append(a + ","));

        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(
                rolesAndAuthorities.toString());

        return authorities;
    }

    public ZonedDateTime getGlobalBlockUntil() {
        return globalBlockUntil;
    }

    public void setGlobalBlockUntil(ZonedDateTime globalBlockUntil) {
        this.globalBlockUntil = globalBlockUntil;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
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
        return !isDeleted;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isDeleted;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isDeleted;
    }

    @Override
    public boolean isEnabled() {
        return !isDeleted;
    }
}
