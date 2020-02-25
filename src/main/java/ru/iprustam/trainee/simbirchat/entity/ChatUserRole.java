package ru.iprustam.trainee.simbirchat.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class UserRole {
    @Id
    private Short roleId;
    private String roleName;
}
