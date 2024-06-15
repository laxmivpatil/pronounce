package com.abcm.jwt.entity;

import javax.persistence.*;
import java.util.List;



@Entity
public class Accent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accentName;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

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

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}
    
    
    
    

    // Constructors, getters, and setters
}

