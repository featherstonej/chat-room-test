package com.example.chat_middleware.dao.entities;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "message_edits")
public class MessageEdits {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "time", nullable = false)
    private Instant time;

    @Column(name = "modification", columnDefinition = "TEXT", nullable = false)
    private String modification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_message_uuid", nullable = false)
    private Messages originalMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editor_uuid")
    private Users editor;

    public MessageEdits() {}

    public MessageEdits(UUID id, Instant time, String modification, Messages originalMessage, Users editor) {
        this.id = id;
        this.time = time;
        this.modification = modification;
        this.originalMessage = originalMessage;
        this.editor = editor;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getModification() {
        return modification;
    }

    public void setModification(String modification) {
        this.modification = modification;
    }

    public Messages getOriginalMessage() {
        return originalMessage;
    }

    public void setOriginalMessage(Messages originalMessage) {
        this.originalMessage = originalMessage;
    }

    public Users getEditor() {
        return editor;
    }

    public void setEditor(Users editor) {
        this.editor = editor;
    }
}
