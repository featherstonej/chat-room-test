package com.example.chat_middleware.dao.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_conversations", uniqueConstraints = {
    @UniqueConstraint(name = "uk_user_conversation", columnNames = {"user_uuid", "conversation_uuid"})
})
public class UserConversations {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_uuid", nullable = false)
    private Conversations conversation;

    @Column(name = "personal_settings", columnDefinition = "TEXT")
    private String personalSettings;

    @Column(name = "permission_override")
    private String permissionOverride;

    public UserConversations() {}

    public UserConversations(UUID id, Users user, Conversations conversation, String personalSettings, String permissionOverride) {
        this.id = id;
        this.user = user;
        this.conversation = conversation;
        this.personalSettings = personalSettings;
        this.permissionOverride = permissionOverride;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Conversations getConversation() {
        return conversation;
    }

    public void setConversation(Conversations conversation) {
        this.conversation = conversation;
    }

    public String getPersonalSettings() {
        return personalSettings;
    }

    public void setPersonalSettings(String personalSettings) {
        this.personalSettings = personalSettings;
    }

    public String getPermissionOverride() {
        return permissionOverride;
    }

    public void setPermissionOverride(String permissionOverride) {
        this.permissionOverride = permissionOverride;
    }
}
