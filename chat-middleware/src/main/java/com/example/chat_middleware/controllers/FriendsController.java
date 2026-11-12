package com.example.chat_middleware.controllers;

import com.example.chat_middleware.dao.UserDao;
import com.example.chat_middleware.dao.UserFriendDao;
import com.example.chat_middleware.dao.entities.UserFriends;
import com.example.chat_middleware.dao.entities.Users;
import com.example.chat_middleware.dto.FriendRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/friends")
@CrossOrigin(origins = "*")
public class FriendsController {

    @Autowired
    private UserFriendDao userFriendDao;

    @Autowired
    private UserDao userDao;

    /**
     * Send a friend request to another user.
     */
    @PostMapping("/request")
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendRequest request) {
        if (request.getSenderId() == null || request.getRecipientId() == null) {
            return ResponseEntity.badRequest().body("Both senderId and recipientId are required");
        }
        if (request.getSenderId().equals(request.getRecipientId())) {
            return ResponseEntity.badRequest().body("You cannot friend yourself");
        }

        Optional<Users> senderOpt = userDao.findById(request.getSenderId());
        Optional<Users> recipientOpt = userDao.findById(request.getRecipientId());

        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sender or recipient user not found");
        }

        // Check if relationship already exists
        Optional<UserFriends> existing = userFriendDao.findFriendshipBetween(request.getSenderId(), request.getRecipientId());
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Friendship or request already exists with status: " + existing.get().getStatus());
        }

        UserFriends friendEntry = new UserFriends();
        friendEntry.setPerson1(senderOpt.get());
        friendEntry.setPerson2(recipientOpt.get());
        friendEntry.setStatus("PENDING");
        friendEntry.setCreatedAt(Instant.now());

        UserFriends saved = userFriendDao.save(friendEntry);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Accept a pending friend request.
     */
    @PutMapping("/{id}/accept")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable UUID id) {
        Optional<UserFriends> requestOpt = userFriendDao.findById(id);
        if (requestOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Friend request not found");
        }

        UserFriends friendship = requestOpt.get();
        friendship.setStatus("ACCEPTED");
        UserFriends updated = userFriendDao.save(friendship);
        return ResponseEntity.ok(updated);
    }

    /**
     * Reject or decline a friend request.
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectFriendRequest(@PathVariable UUID id) {
        Optional<UserFriends> requestOpt = userFriendDao.findById(id);
        if (requestOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Friend request not found");
        }

        UserFriends friendship = requestOpt.get();
        friendship.setStatus("REJECTED");
        UserFriends updated = userFriendDao.save(friendship);
        return ResponseEntity.ok(updated);
    }

    /**
     * List all accepted friends for a user.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserFriends>> getFriendsList(@PathVariable UUID userId) {
        List<UserFriends> friends = userFriendDao.findByUserIdAndStatus(userId, "ACCEPTED");
        return ResponseEntity.ok(friends);
    }

    /**
     * List incoming pending friend requests received by a user.
     */
    @GetMapping("/requests/pending/{userId}")
    public ResponseEntity<List<UserFriends>> getPendingReceivedRequests(@PathVariable UUID userId) {
        List<UserFriends> pending = userFriendDao.findByPerson2_IdAndStatus(userId, "PENDING");
        return ResponseEntity.ok(pending);
    }

    /**
     * List outgoing friend requests sent by a user.
     */
    @GetMapping("/requests/sent/{userId}")
    public ResponseEntity<List<UserFriends>> getPendingSentRequests(@PathVariable UUID userId) {
        List<UserFriends> sent = userFriendDao.findByPerson1_IdAndStatus(userId, "PENDING");
        return ResponseEntity.ok(sent);
    }

    /**
     * Remove a friend or cancel a friend request.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFriendship(@PathVariable UUID id) {
        if (!userFriendDao.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Friendship record not found");
        }
        userFriendDao.deleteById(id);
        return ResponseEntity.ok("Friendship removed");
    }

    /**
     * Check if two users are friends.
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkFriendship(
            @RequestParam UUID user1Id,
            @RequestParam UUID user2Id) {
        boolean friends = userFriendDao.areFriends(user1Id, user2Id, "ACCEPTED");
        return ResponseEntity.ok(friends);
    }
}
