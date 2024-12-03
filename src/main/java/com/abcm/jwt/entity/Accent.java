package com.abcm.jwt.entity;

 

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

 









@Entity
public class Accent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String countryName;
    private String accentName; // Name of the accent
    @Column(length = 1024)  // Set a longer length for the 'flag' column
    private String flag; // URL or path to the accent's flag image
    private String code;
    
    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @OneToOne(cascade = CascadeType.ALL)  // Add CascadeType.ALL to ensure prompt is saved along with accent
    @JoinColumn(name = "prompt_id")
    private Prompt prompt;

    
    
    
    public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	// Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccentName() {
        return accentName;
    }

    public void setAccentName(String accentName) {
        this.accentName = accentName;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

	public Prompt getPrompt() {
		return prompt;
	}

	public void setPrompt(Prompt prompt) {
		this.prompt = prompt;
	}
    
}
