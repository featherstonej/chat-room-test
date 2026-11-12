package com.example.chat_middleware.dto;

import java.util.UUID;

public class FriendRequest {
    private UUID senderId;
    private UUID recipientId;

    public FriendRequest() {}

    public FriendRequest(UUID senderId, UUID recipientId) {
        this.senderId = senderId;
        this.recipientId = recipientId;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(UUID recipientId) {
        this.recipientId = recipientId;
    }
}
