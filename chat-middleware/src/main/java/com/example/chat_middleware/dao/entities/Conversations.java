package com.example.chat_middleware.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "conversations")
public class Conversations {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "conversation_name")
    private String conversationName;

    @Column(name = "world_permissions")
    private String worldPermissions;

    @JsonIgnore
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Messages> messages = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserConversations> userConversations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupConversations> groupConversations = new ArrayList<>();

    public Conversations() {}

    public Conversations(UUID id, Instant startTime, String conversationName, String worldPermissions) {
        this.id = id;
        this.startTime = startTime;
        this.conversationName = conversationName;
        this.worldPermissions = worldPermissions;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public String getWorldPermissions() {
        return worldPermissions;
    }

    public void setWorldPermissions(String worldPermissions) {
        this.worldPermissions = worldPermissions;
    }

    public List<Messages> getMessages() {
        return messages;
    }

    public void setMessages(List<Messages> messages) {
        this.messages = messages;
    }

    public List<UserConversations> getUserConversations() {
        return userConversations;
    }

    public void setUserConversations(List<UserConversations> userConversations) {
        this.userConversations = userConversations;
    }

    public List<GroupConversations> getGroupConversations() {
        return groupConversations;
    }

    public void setGroupConversations(List<GroupConversations> groupConversations) {
        this.groupConversations = groupConversations;
    }
}
