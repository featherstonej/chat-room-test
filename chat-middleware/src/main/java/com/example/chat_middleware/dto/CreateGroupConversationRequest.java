package com.example.chat_middleware.dto;

import java.util.UUID;

public class CreateGroupConversationRequest {
    private UUID groupId;
    private String conversationName;
    private String worldPermissions;

    public CreateGroupConversationRequest() {}

    public CreateGroupConversationRequest(UUID groupId, String conversationName, String worldPermissions) {
        this.groupId = groupId;
        this.conversationName = conversationName;
        this.worldPermissions = worldPermissions;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
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
}
