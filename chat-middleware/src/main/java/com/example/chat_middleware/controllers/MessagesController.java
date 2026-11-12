package com.example.chat_middleware.controllers;

import com.example.chat_middleware.dao.ConversationDao;
import com.example.chat_middleware.dao.MessageDao;
import com.example.chat_middleware.dao.MessageEditDao;
import com.example.chat_middleware.dao.UserDao;
import com.example.chat_middleware.dao.entities.Conversations;
import com.example.chat_middleware.dao.entities.MessageEdits;
import com.example.chat_middleware.dao.entities.Messages;
import com.example.chat_middleware.dao.entities.Users;
import com.example.chat_middleware.dto.EditMessageRequest;
import com.example.chat_middleware.dto.SendMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "*")
public class MessagesController {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private MessageEditDao messageEditDao;

    @Autowired
    private ConversationDao conversationDao;

    @Autowired
    private UserDao userDao;

    /**
     * Send a new message to a conversation.
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody SendMessageRequest request) {
        if (request.getConversationId() == null || request.getAuthorId() == null) {
            return ResponseEntity.badRequest().body("Conversation ID and Author ID are required");
        }
        if (request.getMessageContents() == null || request.getMessageContents().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Message content cannot be empty");
        }

        Optional<Conversations> convOpt = conversationDao.findById(request.getConversationId());
        if (convOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conversation not found");
        }

        Optional<Users> authorOpt = userDao.findById(request.getAuthorId());
        if (authorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Author not found");
        }

        Messages replyTo = null;
        if (request.getReplyToId() != null) {
            Optional<Messages> replyOpt = messageDao.findById(request.getReplyToId());
            if (replyOpt.isPresent()) {
                replyTo = replyOpt.get();
            }
        }

        Messages message = new Messages();
        message.setConversation(convOpt.get());
        message.setAuthor(authorOpt.get());
        message.setMessageContents(request.getMessageContents());
        message.setTime(Instant.now());
        message.setReplyTo(replyTo);

        Messages savedMessage = messageDao.save(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
    }

    /**
     * Get all messages in a conversation in chronological order.
     */
    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<?> getConversationMessages(
            @PathVariable UUID conversationId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (!conversationDao.existsById(conversationId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conversation not found");
        }

        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("time").descending());
            Page<Messages> messagePage = messageDao.findByConversation_IdOrderByTimeDesc(conversationId, pageable);
            return ResponseEntity.ok(messagePage);
        }

        List<Messages> messages = messageDao.findByConversation_IdOrderByTimeAsc(conversationId);
        return ResponseEntity.ok(messages);
    }

    /**
     * Get a single message by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getMessageById(@PathVariable UUID id) {
        Optional<Messages> messageOpt = messageDao.findById(id);
        if (messageOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message not found");
        }
        return ResponseEntity.ok(messageOpt.get());
    }

    /**
     * Edit a message and automatically record an audit entry in MessageEdits.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> editMessage(@PathVariable UUID id, @RequestBody EditMessageRequest request) {
        Optional<Messages> messageOpt = messageDao.findById(id);
        if (messageOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message not found");
        }

        if (request.getNewContent() == null || request.getNewContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("New content cannot be empty");
        }

        Messages message = messageOpt.get();
        Users editor = null;
        if (request.getEditorId() != null) {
            Optional<Users> editorOpt = userDao.findById(request.getEditorId());
            if (editorOpt.isPresent()) {
                editor = editorOpt.get();
            }
        }

        // Record edit in audit history
        MessageEdits edit = new MessageEdits();
        edit.setOriginalMessage(message);
        edit.setEditor(editor);
        edit.setModification(request.getNewContent());
        edit.setTime(Instant.now());
        messageEditDao.save(edit);

        // Update current message
        message.setMessageContents(request.getNewContent());
        message.setEditTime(Instant.now());
        Messages updatedMessage = messageDao.save(message);

        return ResponseEntity.ok(updatedMessage);
    }

    /**
     * Get the revision history of an edited message.
     */
    @GetMapping("/{id}/edits")
    public ResponseEntity<?> getMessageEdits(@PathVariable UUID id) {
        if (!messageDao.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message not found");
        }
        List<MessageEdits> edits = messageEditDao.findByOriginalMessage_IdOrderByTimeAsc(id);
        return ResponseEntity.ok(edits);
    }

    /**
     * Get replies in a thread for a parent message.
     */
    @GetMapping("/{id}/replies")
    public ResponseEntity<?> getThreadReplies(@PathVariable UUID id) {
        if (!messageDao.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message not found");
        }
        List<Messages> replies = messageDao.findByReplyTo_IdOrderByTimeAsc(id);
        return ResponseEntity.ok(replies);
    }

    /**
     * Search within a conversation's messages by keyword.
     */
    @GetMapping("/conversation/{conversationId}/search")
    public ResponseEntity<List<Messages>> searchMessages(
            @PathVariable UUID conversationId,
            @RequestParam String query) {
        List<Messages> matches = messageDao.findByConversation_IdAndMessageContentsContainingIgnoreCaseOrderByTimeAsc(conversationId, query);
        return ResponseEntity.ok(matches);
    }

    /**
     * Delete a message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable UUID id) {
        if (!messageDao.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message not found");
        }
        messageDao.deleteById(id);
        return ResponseEntity.ok("Message deleted");
    }
}
