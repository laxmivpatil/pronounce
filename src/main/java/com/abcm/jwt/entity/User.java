package com.abcm.jwt.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.Getter;

@Entity
@Data
@Getter
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	 @Enumerated(EnumType.STRING)
	    private AuthProvider authProvider; // GOOGLE or LOCAL
	private String username="";
	private String password;
	private String email="";
	 private LocalDate birthDate;
	    private String gender="";
	    private String location="";
	    private String education="";
	    private String mobileNo="";
	    private String nativeLanguage="";
	    private String profile="";
	    
	    @OneToOne
	    private AppLanguage appLanguage;
	    
	    
	    // List to store exactly three accents in the order of priority (1, 2, 3)
	    @ElementCollection
	    @CollectionTable(name = "user_accents", joinColumns = @JoinColumn(name = "user_id"))
	    @Column(name = "accent_id")
	    private List<Long> accentIds;

	    // Getter and setter for accentIds
	    public List<Long> getAccentIds() {
	        return accentIds;
	    }

	    public void setAccentIds(List<Long> accentIds) {
	        this.accentIds = accentIds;
	    }
	
	    public LocalDate getBirthDate() {
			return birthDate;
		}
		public void setBirthDate(LocalDate birthDate) {
			this.birthDate = birthDate;
		}
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public String getEducation() {
			return education;
		}
		public void setEducation(String education) {
			this.education = education;
		}
		public String getMobileNo() {
			return mobileNo;
		}
		public void setMobileNo(String mobileNo) {
			this.mobileNo = mobileNo;
		}
		public String getNativeLanguage() {
			return nativeLanguage;
		}
		public void setNativeLanguage(String nativeLanguage) {
			this.nativeLanguage = nativeLanguage;
		}
		public String getProfile() {
			return profile;
		}
		public void setProfile(String profile) {
			this.profile = profile;
		}
	 
	public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public AppLanguage getAppLanguage() {
		return appLanguage;
	}
	public void setAppLanguage(AppLanguage appLanguage) {
		this.appLanguage = appLanguage;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email
				+ ", birthDate=" + birthDate + ", gender=" + gender + ", location=" + location + ", education="
				+ education + ", mobileNo=" + mobileNo + ", nativeLanguage=" + nativeLanguage + ", profile=" + profile
				+ ", appLanguage=" + appLanguage + "]";
	}

	public AuthProvider getAuthProvider() {
		return authProvider;
	}

	public void setAuthProvider(AuthProvider authProvider) {
		this.authProvider = authProvider;
	}
	
	
	
	

}
