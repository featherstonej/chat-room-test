package com.example.chat_middleware.dto;

public class UpdateProfileRequest {
    private String profileDetails;

    public UpdateProfileRequest() {}

    public UpdateProfileRequest(String profileDetails) {
        this.profileDetails = profileDetails;
    }

    public String getProfileDetails() {
        return profileDetails;
    }

    public void setProfileDetails(String profileDetails) {
        this.profileDetails = profileDetails;
    }
}
