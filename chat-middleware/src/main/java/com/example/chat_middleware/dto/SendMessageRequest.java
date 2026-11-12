package com.example.chat_middleware.dto;

import java.util.UUID;

public class SendMessageRequest {
    private UUID conversationId;
    private UUID authorId;
    private String messageContents;
    private UUID replyToId;

    public SendMessageRequest() {}

    public SendMessageRequest(UUID conversationId, UUID authorId, String messageContents, UUID replyToId) {
        this.conversationId = conversationId;
        this.authorId = authorId;
        this.messageContents = messageContents;
        this.replyToId = replyToId;
    }

    public UUID getConversationId() {
        return conversationId;
    }

    public void setConversationId(UUID conversationId) {
        this.conversationId = conversationId;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public String getMessageContents() {
        return messageContents;
    }

    public void setMessageContents(String messageContents) {
        this.messageContents = messageContents;
    }

    public UUID getReplyToId() {
        return replyToId;
    }

    public void setReplyToId(UUID replyToId) {
        this.replyToId = replyToId;
    }
}
