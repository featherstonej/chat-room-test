package com.example.chat_middleware.dao;

import com.example.chat_middleware.dao.entities.Messages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageDao extends JpaRepository<Messages, UUID> {

    /**
     * Retrieve all messages in a conversation in chronological order (oldest to newest).
     *
     * @param conversationId the conversation UUID
     * @return list of messages
     */
    List<Messages> findByConversation_IdOrderByTimeAsc(UUID conversationId);

    /**
     * Retrieve all messages in a conversation with newest messages first.
     *
     * @param conversationId the conversation UUID
     * @return list of messages
     */
    List<Messages> findByConversation_IdOrderByTimeDesc(UUID conversationId);

    /**
     * Retrieve paginated messages for a conversation (useful for infinite scrolling).
     *
     * @param conversationId the conversation UUID
     * @param pageable pagination and sorting parameters
     * @return a page of messages
     */
    Page<Messages> findByConversation_IdOrderByTimeDesc(UUID conversationId, Pageable pageable);

    /**
     * Retrieve messages sent before a specific timestamp (cursor-based pagination for chat history).
     *
     * @param conversationId the conversation UUID
     * @param before timestamp cursor
     * @param pageable pagination parameters (e.g., limit)
     * @return list of messages
     */
    List<Messages> findByConversation_IdAndTimeBeforeOrderByTimeDesc(UUID conversationId, Instant before, Pageable pageable);

    /**
     * Retrieve all messages authored by a specific user.
     *
     * @param authorId the user's UUID
     * @return list of messages authored by the user
     */
    List<Messages> findByAuthor_IdOrderByTimeDesc(UUID authorId);

    /**
     * Retrieve all reply messages in a thread for a given parent message.
     *
     * @param replyToId the parent message UUID
     * @return list of reply messages in chronological order
     */
    List<Messages> findByReplyTo_IdOrderByTimeAsc(UUID replyToId);

    /**
     * Search within a conversation's messages by keyword (case-insensitive).
     *
     * @param conversationId the conversation UUID
     * @param keyword the search term
     * @return list of matching messages
     */
    List<Messages> findByConversation_IdAndMessageContentsContainingIgnoreCaseOrderByTimeAsc(UUID conversationId, String keyword);

    /**
     * Get the total count of messages in a conversation.
     *
     * @param conversationId the conversation UUID
     * @return message count
     */
    long countByConversation_Id(UUID conversationId);

    /**
     * Fetch the most recent message in a conversation (useful for chat list preview snippets).
     *
     * @param conversationId the conversation UUID
     * @return Optional containing the latest message
     */
    Optional<Messages> findTopByConversation_IdOrderByTimeDesc(UUID conversationId);
}
