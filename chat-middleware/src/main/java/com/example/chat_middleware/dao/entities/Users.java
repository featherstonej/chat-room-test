package com.example.chat_middleware.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "user_name", unique = true, nullable = false)
    private String userName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "profile_details", columnDefinition = "TEXT")
    private String profileDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupUsers> groupUsers = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserConversations> userConversations = new ArrayList<>();

    public Users() {}

    public Users(UUID id, String userName, String password, String profileDetails) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.profileDetails = profileDetails;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileDetails() {
        return profileDetails;
    }

    public void setProfileDetails(String profileDetails) {
        this.profileDetails = profileDetails;
    }

    public List<GroupUsers> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(List<GroupUsers> groupUsers) {
        this.groupUsers = groupUsers;
    }

    public List<UserConversations> getUserConversations() {
        return userConversations;
    }

    public void setUserConversations(List<UserConversations> userConversations) {
        this.userConversations = userConversations;
    }
}
