package com.example.chat_middleware.dao.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "group_conversations", uniqueConstraints = {
    @UniqueConstraint(name = "uk_group_conversation", columnNames = {"group_uuid", "conversation_uuid"})
})
public class GroupConversations {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_uuid", nullable = false)
    private Groups group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_uuid", nullable = false)
    private Conversations conversation;

    @Column(name = "permission_override")
    private String permissionOverride;

    public GroupConversations() {}

    public GroupConversations(UUID id, Groups group, Conversations conversation, String permissionOverride) {
        this.id = id;
        this.group = group;
        this.conversation = conversation;
        this.permissionOverride = permissionOverride;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Groups getGroup() {
        return group;
    }

    public void setGroup(Groups group) {
        this.group = group;
    }

    public Conversations getConversation() {
        return conversation;
    }

    public void setConversation(Conversations conversation) {
        this.conversation = conversation;
    }

    public String getPermissionOverride() {
        return permissionOverride;
    }

    public void setPermissionOverride(String permissionOverride) {
        this.permissionOverride = permissionOverride;
    }
}
