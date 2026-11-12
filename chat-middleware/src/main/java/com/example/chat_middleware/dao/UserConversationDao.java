package com.example.chat_middleware.dao;

import com.example.chat_middleware.dao.entities.UserConversations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserConversationDao extends JpaRepository<UserConversations, UUID> {

    /**
     * Retrieve all conversation memberships and settings for a specific user.
     *
     * @param userId the user's UUID
     * @return list of user conversations
     */
    List<UserConversations> findByUser_Id(UUID userId);

    /**
     * Retrieve all user participants in a conversation.
     *
     * @param conversationId the conversation UUID
     * @return list of user participants
     */
    List<UserConversations> findByConversation_Id(UUID conversationId);

    /**
     * Retrieve a user's personal settings and permissions for a specific conversation.
     *
     * @param userId the user's UUID
     * @param conversationId the conversation UUID
     * @return Optional containing the UserConversations record
     */
    Optional<UserConversations> findByUser_IdAndConversation_Id(UUID userId, UUID conversationId);

    /**
     * Check if a user is a participant in a conversation.
     *
     * @param userId the user's UUID
     * @param conversationId the conversation UUID
     * @return true if participant, false otherwise
     */
    boolean existsByUser_IdAndConversation_Id(UUID userId, UUID conversationId);

    /**
     * Remove a user from a conversation (leave conversation).
     *
     * @param userId the user's UUID
     * @param conversationId the conversation UUID
     */
    void deleteByUser_IdAndConversation_Id(UUID userId, UUID conversationId);
}
