package ru.iprustam.trainee.simbirchat.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Table(name = "role")
public class ChatUserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short roleId;
    private String roleName;
    @ColumnDefault("0")
    private Short authorities;

    public Short getRoleId() {
        return roleId;
    }

    public void setRoleId(Short roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Short getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Short authorities) {
        this.authorities = authorities;
    }
}
