package com.example.chat_middleware.dao;

import com.example.chat_middleware.dao.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDao extends JpaRepository<Users, UUID> {

    /**
     * Find a user by their exact username.
     *
     * @param userName the unique username
     * @return an Optional containing the User if found
     */
    Optional<Users> findByUserName(String userName);

    /**
     * Authenticate or find a user by their username and password.
     *
     * @param userName the unique username
     * @param password the user's password
     * @return the User entity if credentials match, or null
     */
    Users findByUserNameAndPassword(String userName, String password);

    /**
     * Check if a username is already taken.
     *
     * @param userName username to check
     * @return true if taken, false otherwise
     */
    boolean existsByUserName(String userName);

    /**
     * Search users by username substring (case-insensitive) for user discovery and search.
     *
     * @param userName search keyword
     * @return list of matching Users
     */
    List<Users> findByUserNameContainingIgnoreCase(String userName);
}
