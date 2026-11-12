package com.example.chat_middleware.dto;

import java.util.UUID;

public class EditMessageRequest {
    private UUID editorId;
    private String newContent;

    public EditMessageRequest() {}

    public EditMessageRequest(UUID editorId, String newContent) {
        this.editorId = editorId;
        this.newContent = newContent;
    }

    public UUID getEditorId() {
        return editorId;
    }

    public void setEditorId(UUID editorId) {
        this.editorId = editorId;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }
}
