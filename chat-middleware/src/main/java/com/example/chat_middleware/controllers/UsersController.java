package com.example.chat_middleware.controllers;

import com.example.chat_middleware.dao.UserDao;
import com.example.chat_middleware.dao.entities.Users;
import com.example.chat_middleware.dto.LoginRequest;
import com.example.chat_middleware.dto.UpdateProfileRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UsersController {

    @Autowired
    private UserDao userDao;

    /**
     * Register or add a new user.
     */
    @Deprecated 
    @PostMapping(path = "/add")
    public ResponseEntity<?> addNewUser(@RequestBody Users user) {
        if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Username cannot be empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Password cannot be empty");
        }
        if (userDao.existsByUserName(user.getUserName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        Users savedUser = userDao.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    /**
     * Get user by username and password (legacy GET endpoint).
     */
    @GetMapping(path = "/user")
    public ResponseEntity<?> getUserByUserName(@RequestParam String userName, @RequestParam String password) {
        Users user = userDao.findByUserNameAndPassword(userName, password);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Authenticate user with JSON body credentials.
     */
    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Users user = userDao.findByUserNameAndPassword(loginRequest.getUserName(), loginRequest.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Retrieve user profile by user UUID.
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        Optional<Users> user = userDao.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok(user.get());
    }

    /**
     * Check if a username is available for registration.
     */
    @GetMapping(path = "/check-username")
    public ResponseEntity<?> checkUsernameAvailability(@RequestParam String username) {
        boolean exists = userDao.existsByUserName(username);
        return ResponseEntity.ok(!exists);
    }

    /**
     * Search users by partial username match.
     */
    @GetMapping(path = "/search")
    public ResponseEntity<List<Users>> searchUsers(@RequestParam String query) {
        List<Users> users = userDao.findByUserNameContainingIgnoreCase(query);
        return ResponseEntity.ok(users);
    }

    /**
     * Update user profile details.
     */
    @PutMapping(path = "/{id}/profile")
    public ResponseEntity<?> updateProfile(@PathVariable UUID id, @RequestBody UpdateProfileRequest request) {
        Optional<Users> userOpt = userDao.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Users user = userOpt.get();
        user.setProfileDetails(request.getProfileDetails());
        Users updatedUser = userDao.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * List all users.
     */
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userDao.findAll();
        return ResponseEntity.ok(users);
    }
}
