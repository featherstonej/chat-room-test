package com.example.chat_middleware.security;

import java.util.UUID;

public class ChatUserPrincipal extends org.springframework.security.core.userdetails.User {

    private final UUID id;

    public ChatUserPrincipal(UUID id, String username, String password) {
        super(username, password, java.util.Collections.emptyList());
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
