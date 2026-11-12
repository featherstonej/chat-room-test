package com.example.chat_middleware.dao;

import com.example.chat_middleware.dao.entities.GroupConversations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupConversationDao extends JpaRepository<GroupConversations, UUID> {

    /**
     * Retrieve all conversation mappings for a given group.
     *
     * @param groupId the group's UUID
     * @return list of group conversations
     */
    List<GroupConversations> findByGroup_Id(UUID groupId);

    /**
     * Retrieve all group mappings for a given conversation.
     *
     * @param conversationId the conversation UUID
     * @return list of group conversations
     */
    List<GroupConversations> findByConversation_Id(UUID conversationId);

    /**
     * Find the relationship and permission override between a group and a conversation.
     *
     * @param groupId the group's UUID
     * @param conversationId the conversation UUID
     * @return Optional containing GroupConversations if exists
     */
    Optional<GroupConversations> findByGroup_IdAndConversation_Id(UUID groupId, UUID conversationId);

    /**
     * Check if a conversation is linked to a group.
     *
     * @param groupId the group's UUID
     * @param conversationId the conversation UUID
     * @return true if linked, false otherwise
     */
    boolean existsByGroup_IdAndConversation_Id(UUID groupId, UUID conversationId);

    /**
     * Remove a conversation association from a group.
     *
     * @param groupId the group's UUID
     * @param conversationId the conversation UUID
     */
    void deleteByGroup_IdAndConversation_Id(UUID groupId, UUID conversationId);
}
