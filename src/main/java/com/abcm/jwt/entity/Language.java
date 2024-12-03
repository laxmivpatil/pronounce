package com.abcm.jwt.entity;

 

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Entity
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String languageName; // Name of the language

   
    @OneToMany(mappedBy = "language", cascade = CascadeType.ALL)
    @JsonManagedReference  // Prevents cyclic reference by allowing serialization on the parent side
    private List<Accent> accents; // List of accents for this language

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public List<Accent> getAccents() {
        return accents;
    }

    public void setAccents(List<Accent> accents) {
        this.accents = accents;
    }
}
