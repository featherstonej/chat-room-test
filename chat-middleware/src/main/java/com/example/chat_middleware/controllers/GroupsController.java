package com.example.chat_middleware.controllers;

import com.example.chat_middleware.dao.GroupConversationDao;
import com.example.chat_middleware.dao.GroupDao;
import com.example.chat_middleware.dao.GroupUserDao;
import com.example.chat_middleware.dao.UserDao;
import com.example.chat_middleware.dao.entities.GroupConversations;
import com.example.chat_middleware.dao.entities.GroupUsers;
import com.example.chat_middleware.dao.entities.Groups;
import com.example.chat_middleware.dao.entities.Users;
import com.example.chat_middleware.dto.AddGroupMemberRequest;
import com.example.chat_middleware.dto.CreateGroupRequest;
import com.example.chat_middleware.dto.UpdateMemberRoleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/groups")
@CrossOrigin(origins = "*")
public class GroupsController {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private GroupUserDao groupUserDao;

    @Autowired
    private GroupConversationDao groupConversationDao;

    @Autowired
    private UserDao userDao;

    /**
     * Create a new group and automatically assign the creator as ADMIN.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Group name cannot be empty");
        }
        if (request.getCreatorId() == null) {
            return ResponseEntity.badRequest().body("Creator ID is required");
        }

        Optional<Users> creatorOpt = userDao.findById(request.getCreatorId());
        if (creatorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Creator user not found");
        }

        Groups group = new Groups();
        group.setName(request.getName());
        group.setGroupDetails(request.getGroupDetails());
        Groups savedGroup = groupDao.save(group);

        // Add creator as ADMIN
        GroupUsers adminMember = new GroupUsers();
        adminMember.setGroup(savedGroup);
        adminMember.setUser(creatorOpt.get());
        adminMember.setRole("ADMIN");
        groupUserDao.save(adminMember);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedGroup);
    }

    /**
     * Get all groups a user is a member of.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Groups>> getUserGroups(@PathVariable UUID userId) {
        List<Groups> groups = groupDao.findGroupsByUserId(userId);
        return ResponseEntity.ok(groups);
    }

    /**
     * Get group details by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getGroupById(@PathVariable UUID id) {
        Optional<Groups> group = groupDao.findById(id);
        if (group.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
        }
        return ResponseEntity.ok(group.get());
    }

    /**
     * List all members of a group.
     */
    @GetMapping("/{id}/members")
    public ResponseEntity<List<GroupUsers>> getGroupMembers(@PathVariable UUID id) {
        List<GroupUsers> members = groupUserDao.findByGroup_Id(id);
        return ResponseEntity.ok(members);
    }

    /**
     * Add a member to a group.
     */
    @PostMapping("/{id}/members")
    public ResponseEntity<?> addMemberToGroup(@PathVariable UUID id, @RequestBody AddGroupMemberRequest request) {
        Optional<Groups> groupOpt = groupDao.findById(id);
        Optional<Users> userOpt = userDao.findById(request.getUserId());

        if (groupOpt.isEmpty() || userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group or User not found");
        }

        if (groupUserDao.existsByGroup_IdAndUser_Id(id, request.getUserId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already in group");
        }

        GroupUsers member = new GroupUsers();
        member.setGroup(groupOpt.get());
        member.setUser(userOpt.get());
        member.setRole(request.getRole() != null ? request.getRole() : "MEMBER");
        GroupUsers saved = groupUserDao.save(member);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Update a member's role in a group.
     */
    @PutMapping("/{id}/members/{userId}/role")
    public ResponseEntity<?> updateMemberRole(
            @PathVariable UUID id,
            @PathVariable UUID userId,
            @RequestBody UpdateMemberRoleRequest request) {

        Optional<GroupUsers> membershipOpt = groupUserDao.findByGroup_IdAndUser_Id(id, userId);
        if (membershipOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not a member of this group");
        }

        GroupUsers membership = membershipOpt.get();
        membership.setRole(request.getRole());
        GroupUsers updated = groupUserDao.save(membership);
        return ResponseEntity.ok(updated);
    }

    /**
     * Remove a member from a group (leave or kick).
     */
    @Transactional
    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<?> removeMemberFromGroup(@PathVariable UUID id, @PathVariable UUID userId) {
        if (!groupUserDao.existsByGroup_IdAndUser_Id(id, userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not a member of this group");
        }

        groupUserDao.deleteByGroup_IdAndUser_Id(id, userId);
        return ResponseEntity.ok("Member removed from group");
    }

    /**
     * List all conversations and channels belonging to a group.
     */
    @GetMapping("/{id}/conversations")
    public ResponseEntity<List<GroupConversations>> getGroupConversations(@PathVariable UUID id) {
        List<GroupConversations> conversations = groupConversationDao.findByGroup_Id(id);
        return ResponseEntity.ok(conversations);
    }

    /**
     * Search groups by name.
     */
    @GetMapping("/search")
    public ResponseEntity<List<Groups>> searchGroups(@RequestParam String query) {
        List<Groups> groups = groupDao.findByNameContainingIgnoreCase(query);
        return ResponseEntity.ok(groups);
    }
}
