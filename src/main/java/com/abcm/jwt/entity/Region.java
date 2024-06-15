package com.abcm.jwt.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String logo;

    private String regionName;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
    private List<Accent> accents;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public List<Accent> getAccents() {
		return accents;
	}

	public void setAccents(List<Accent> accents) {
		this.accents = accents;
	}

    // Constructors, getters, and setters
    
    
    
    
    
}