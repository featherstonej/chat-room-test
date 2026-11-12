package com.example.chat_middleware.dao;

import com.example.chat_middleware.dao.entities.MessageEdits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageEditDao extends JpaRepository<MessageEdits, UUID> {

    /**
     * Retrieve the complete edit history for a specific message in chronological order.
     *
     * @param originalMessageId the original message UUID
     * @return list of message edit history records
     */
    List<MessageEdits> findByOriginalMessage_IdOrderByTimeAsc(UUID originalMessageId);

    /**
     * Retrieve edits for a message with the newest edits first.
     *
     * @param originalMessageId the original message UUID
     * @return list of message edit records
     */
    List<MessageEdits> findByOriginalMessage_IdOrderByTimeDesc(UUID originalMessageId);

    /**
     * Find all edits authored by a specific user.
     *
     * @param editorId the editor user UUID
     * @return list of message edits by this user
     */
    List<MessageEdits> findByEditor_IdOrderByTimeDesc(UUID editorId);

    /**
     * Count how many times a message has been edited.
     *
     * @param originalMessageId the original message UUID
     * @return edit count
     */
    long countByOriginalMessage_Id(UUID originalMessageId);
}
