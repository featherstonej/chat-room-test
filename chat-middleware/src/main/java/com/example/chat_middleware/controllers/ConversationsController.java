package com.example.chat_middleware.controllers;

import com.example.chat_middleware.dao.ConversationDao;
import com.example.chat_middleware.dao.GroupDao;
import com.example.chat_middleware.dao.GroupConversationDao;
import com.example.chat_middleware.dao.UserConversationDao;
import com.example.chat_middleware.dao.UserDao;
import com.example.chat_middleware.dao.entities.*;
import com.example.chat_middleware.dto.CreateDirectConversationRequest;
import com.example.chat_middleware.dto.CreateGroupConversationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/conversations")
@CrossOrigin(origins = "*")
public class ConversationsController {

    @Autowired
    private ConversationDao conversationDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private UserConversationDao userConversationDao;

    @Autowired
    private GroupConversationDao groupConversationDao;

    /**
     * Get all conversations accessible to a given user (direct and group channels).
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Conversations>> getConversationsForUser(@PathVariable UUID userId) {
        List<Conversations> conversations = conversationDao.findAllConversationsForUser(userId);
        return ResponseEntity.ok(conversations);
    }

    /**
     * Get conversation details by conversation ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getConversationById(@PathVariable UUID id) {
        Optional<Conversations> conversation = conversationDao.findById(id);
        if (conversation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conversation not found");
        }
        return ResponseEntity.ok(conversation.get());
    }

    /**
     * Create or retrieve an existing 1-on-1 direct conversation between two users.
     */
    @PostMapping("/direct")
    public ResponseEntity<?> createDirectConversation(@RequestBody CreateDirectConversationRequest request) {
        if (request.getUser1Id() == null || request.getUser2Id() == null) {
            return ResponseEntity.badRequest().body("Both user IDs are required");
        }

        Optional<Users> user1Opt = userDao.findById(request.getUser1Id());
        Optional<Users> user2Opt = userDao.findById(request.getUser2Id());

        if (user1Opt.isEmpty() || user2Opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("One or both users not found");
        }

        // Check if direct conversation already exists between these 2 users
        Optional<Conversations> existing = conversationDao.findDirectConversationBetween(request.getUser1Id(), request.getUser2Id());
        if (existing.isPresent()) {
            return ResponseEntity.ok(existing.get());
        }

        // Create new conversation
        String name = request.getConversationName();
        if (name == null || name.trim().isEmpty()) {
            name = user1Opt.get().getUserName() + " & " + user2Opt.get().getUserName();
        }

        Conversations conversation = new Conversations();
        conversation.setConversationName(name);
        conversation.setStartTime(Instant.now());
        conversation.setWorldPermissions("PRIVATE");
        Conversations savedConversation = conversationDao.save(conversation);

        // Associate user1
        UserConversations uc1 = new UserConversations();
        uc1.setUser(user1Opt.get());
        uc1.setConversation(savedConversation);
        userConversationDao.save(uc1);

        // Associate user2
        UserConversations uc2 = new UserConversations();
        uc2.setUser(user2Opt.get());
        uc2.setConversation(savedConversation);
        userConversationDao.save(uc2);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedConversation);
    }

    /**
     * Create a new group conversation or channel under a group.
     */
    @PostMapping("/group")
    public ResponseEntity<?> createGroupConversation(@RequestBody CreateGroupConversationRequest request) {
        if (request.getGroupId() == null) {
            return ResponseEntity.badRequest().body("Group ID is required");
        }

        Optional<Groups> groupOpt = groupDao.findById(request.getGroupId());
        if (groupOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
        }

        Conversations conversation = new Conversations();
        conversation.setConversationName(request.getConversationName());
        conversation.setStartTime(Instant.now());
        conversation.setWorldPermissions(request.getWorldPermissions() != null ? request.getWorldPermissions() : "GROUP_ONLY");
        Conversations savedConversation = conversationDao.save(conversation);

        GroupConversations gc = new GroupConversations();
        gc.setGroup(groupOpt.get());
        gc.setConversation(savedConversation);
        groupConversationDao.save(gc);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedConversation);
    }

    /**
     * Add a user to an existing conversation.
     */
    @PostMapping("/{id}/users/{userId}")
    public ResponseEntity<?> addUserToConversation(@PathVariable UUID id, @PathVariable UUID userId) {
        Optional<Conversations> convOpt = conversationDao.findById(id);
        Optional<Users> userOpt = userDao.findById(userId);

        if (convOpt.isEmpty() || userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conversation or User not found");
        }

        if (userConversationDao.existsByUser_IdAndConversation_Id(userId, id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already in conversation");
        }

        UserConversations uc = new UserConversations();
        uc.setUser(userOpt.get());
        uc.setConversation(convOpt.get());
        userConversationDao.save(uc);

        return ResponseEntity.status(HttpStatus.CREATED).body("User added to conversation");
    }

    /**
     * Remove a user from a conversation (leave conversation).
     */
    @DeleteMapping("/{id}/users/{userId}")
    public ResponseEntity<?> removeUserFromConversation(@PathVariable UUID id, @PathVariable UUID userId) {
        if (!userConversationDao.existsByUser_IdAndConversation_Id(userId, id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not part of this conversation");
        }

        userConversationDao.deleteByUser_IdAndConversation_Id(userId, id);
        return ResponseEntity.ok("User removed from conversation");
    }

    /**
     * List all participant memberships in a conversation.
     */
    @GetMapping("/{id}/participants")
    public ResponseEntity<List<UserConversations>> getParticipants(@PathVariable UUID id) {
        List<UserConversations> participants = userConversationDao.findByConversation_Id(id);
        return ResponseEntity.ok(participants);
    }
}
