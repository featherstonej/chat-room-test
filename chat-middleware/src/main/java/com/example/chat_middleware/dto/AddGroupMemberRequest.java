package com.example.chat_middleware.dto;

import java.util.UUID;

public class AddGroupMemberRequest {
    private UUID userId;
    private String role; // e.g. "MEMBER", "MODERATOR", "ADMIN"

    public AddGroupMemberRequest() {}

    public AddGroupMemberRequest(UUID userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
