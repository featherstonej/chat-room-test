package com.example.chat_middleware.dao.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "group_users", uniqueConstraints = {
    @UniqueConstraint(name = "uk_group_user", columnNames = {"group_uuid", "user_uuid"})
})
public class GroupUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "role")
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_uuid", nullable = false)
    private Groups group;

    public GroupUsers() {}

    public GroupUsers(UUID id, String role, Users user, Groups group) {
        this.id = id;
        this.role = role;
        this.user = user;
        this.group = group;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Groups getGroup() {
        return group;
    }

    public void setGroup(Groups group) {
        this.group = group;
    }
}
