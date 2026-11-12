package com.example.chat_middleware.dao;

import com.example.chat_middleware.dao.entities.Conversations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationDao extends JpaRepository<Conversations, UUID> {

    /**
     * Search conversations by name (case-insensitive).
     *
     * @param name conversation name search term
     * @return list of matching conversations
     */
    List<Conversations> findByConversationNameContainingIgnoreCase(String name);

    /**
     * Find all direct/user conversations that a specific user participates in.
     *
     * @param userId the user's UUID
     * @return list of conversations
     */
    @Query("SELECT uc.conversation FROM UserConversations uc WHERE uc.user.id = :userId")
    List<Conversations> findConversationsByUserId(@Param("userId") UUID userId);

    /**
     * Find all conversations belonging to a specific group.
     *
     * @param groupId the group's UUID
     * @return list of conversations
     */
    @Query("SELECT gc.conversation FROM GroupConversations gc WHERE gc.group.id = :groupId")
    List<Conversations> findConversationsByGroupId(@Param("groupId") UUID groupId);

    /**
     * Find all conversations accessible to a user (both direct user conversations and group conversations).
     *
     * @param userId the user's UUID
     * @return list of distinct conversations accessible to the user
     */
    @Query("SELECT DISTINCT c FROM Conversations c " +
           "LEFT JOIN c.userConversations uc " +
           "LEFT JOIN c.groupConversations gc " +
           "LEFT JOIN gc.group g " +
           "LEFT JOIN g.groupUsers gu " +
           "WHERE uc.user.id = :userId OR gu.user.id = :userId")
    List<Conversations> findAllConversationsForUser(@Param("userId") UUID userId);

    /**
     * Find an existing 1-on-1 direct conversation between two specific users.
     *
     * @param user1Id first user UUID
     * @param user2Id second user UUID
     * @return Optional containing the direct conversation if one exists
     */
    @Query("SELECT uc1.conversation FROM UserConversations uc1 " +
           "JOIN UserConversations uc2 ON uc1.conversation.id = uc2.conversation.id " +
           "WHERE uc1.user.id = :user1Id AND uc2.user.id = :user2Id " +
           "AND SIZE(uc1.conversation.userConversations) = 2 " +
           "AND SIZE(uc1.conversation.groupConversations) = 0")
    Optional<Conversations> findDirectConversationBetween(@Param("user1Id") UUID user1Id, @Param("user2Id") UUID user2Id);
}
