package com.example.chat_middleware.dto;

import java.util.UUID;

public class CreateGroupRequest {
    private String name;
    private String groupDetails;
    private UUID creatorId;

    public CreateGroupRequest() {}

    public CreateGroupRequest(String name, String groupDetails, UUID creatorId) {
        this.name = name;
        this.groupDetails = groupDetails;
        this.creatorId = creatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupDetails() {
        return groupDetails;
    }

    public void setGroupDetails(String groupDetails) {
        this.groupDetails = groupDetails;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }
}
