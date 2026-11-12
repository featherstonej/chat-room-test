package com.example.chat_middleware.dao.entities;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_friends", uniqueConstraints = {
    @UniqueConstraint(name = "uk_user_friends", columnNames = {"person1_uuid", "person2_uuid"})
})
public class UserFriends {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person1_uuid", nullable = false)
    private Users person1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person2_uuid", nullable = false)
    private Users person2;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private Instant createdAt;

    public UserFriends() {}

    public UserFriends(UUID id, Users person1, Users person2, String status, Instant createdAt) {
        this.id = id;
        this.person1 = person1;
        this.person2 = person2;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Users getPerson1() {
        return person1;
    }

    public void setPerson1(Users person1) {
        this.person1 = person1;
    }

    public Users getPerson2() {
        return person2;
    }

    public void setPerson2(Users person2) {
        this.person2 = person2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
