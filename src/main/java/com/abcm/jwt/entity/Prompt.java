package com.abcm.jwt.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Prompt {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;  // Add this id field


    private String model;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "prompt_id") // Foreign key column in Message table
    private List<Message> messages;

    private double temperature;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    // Nested class to represent individual message
  
}
