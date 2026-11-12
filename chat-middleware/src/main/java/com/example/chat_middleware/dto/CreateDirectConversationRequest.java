package com.example.chat_middleware.dto;

import java.util.UUID;

public class CreateDirectConversationRequest {
    private UUID user1Id;
    private UUID user2Id;
    private String conversationName;

    public CreateDirectConversationRequest() {}

    public CreateDirectConversationRequest(UUID user1Id, UUID user2Id, String conversationName) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.conversationName = conversationName;
    }

    public UUID getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(UUID user1Id) {
        this.user1Id = user1Id;
    }

    public UUID getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(UUID user2Id) {
        this.user2Id = user2Id;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }
}
