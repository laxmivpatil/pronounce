package com.abcm.jwt.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PronounciationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Associated user

    
    @ManyToOne
    @JoinColumn(name = "accent_id", nullable = false)
    private Accent accent; // Associated accent

    @Column(nullable = false)
    private String inputPronounce; // User's input

    @Column(nullable = false)
    private String outputPronounce; // Processed output

    private LocalDateTime timestamp; // When the pronunciation was saved
    
    private boolean favorite; // Mark as favorite

    // Getters and setters
    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }


    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Accent getAccent() {
        return accent;
    }

    public void setAccent(Accent accent) {
        this.accent = accent;
    }

    public String getInputPronounce() {
        return inputPronounce;
    }

    public void setInputPronounce(String inputPronounce) {
        this.inputPronounce = inputPronounce;
    }

    public String getOutputPronounce() {
        return outputPronounce;
    }

    public void setOutputPronounce(String outputPronounce) {
        this.outputPronounce = outputPronounce;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
