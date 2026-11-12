package com.example.chat_middleware.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "messages")
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "message_contents", columnDefinition = "TEXT", nullable = false)
    private String messageContents;

    @Column(name = "time", nullable = false)
    private Instant time;

    @Column(name = "edit_time")
    private Instant editTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_uuid", nullable = false)
    private Conversations conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_uuid", nullable = false)
    private Users author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to_uuid")
    private Messages replyTo;

    @JsonIgnore
    @OneToMany(mappedBy = "originalMessage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageEdits> edits = new ArrayList<>();

    public Messages() {}

    public Messages(UUID id, String messageContents, Instant time, Instant editTime, Conversations conversation, Users author, Messages replyTo) {
        this.id = id;
        this.messageContents = messageContents;
        this.time = time;
        this.editTime = editTime;
        this.conversation = conversation;
        this.author = author;
        this.replyTo = replyTo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMessageContents() {
        return messageContents;
    }

    public void setMessageContents(String messageContents) {
        this.messageContents = messageContents;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public Instant getEditTime() {
        return editTime;
    }

    public void setEditTime(Instant editTime) {
        this.editTime = editTime;
    }

    public Conversations getConversation() {
        return conversation;
    }

    public void setConversation(Conversations conversation) {
        this.conversation = conversation;
    }

    public Users getAuthor() {
        return author;
    }

    public void setAuthor(Users author) {
        this.author = author;
    }

    public Messages getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Messages replyTo) {
        this.replyTo = replyTo;
    }

    public List<MessageEdits> getEdits() {
        return edits;
    }

    public void setEdits(List<MessageEdits> edits) {
        this.edits = edits;
    }
}
