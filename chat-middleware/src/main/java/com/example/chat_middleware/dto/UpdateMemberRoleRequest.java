package com.example.chat_middleware.dto;

public class UpdateMemberRoleRequest {
    private String role; // e.g. "MEMBER", "MODERATOR", "ADMIN"

    public UpdateMemberRoleRequest() {}

    public UpdateMemberRoleRequest(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
