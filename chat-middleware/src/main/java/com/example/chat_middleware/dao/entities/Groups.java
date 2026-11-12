package com.example.chat_middleware.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "groups")
public class Groups {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "group_details", columnDefinition = "TEXT")
    private String groupDetails;

    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupUsers> groupUsers = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupConversations> groupConversations = new ArrayList<>();

    public Groups() {}

    public Groups(UUID id, String name, String groupDetails) {
        this.id = id;
        this.name = name;
        this.groupDetails = groupDetails;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public List<GroupUsers> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(List<GroupUsers> groupUsers) {
        this.groupUsers = groupUsers;
    }

    public List<GroupConversations> getGroupConversations() {
        return groupConversations;
    }

    public void setGroupConversations(List<GroupConversations> groupConversations) {
        this.groupConversations = groupConversations;
    }
}
