 

package com.abcm.jwt.DTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

import org.springframework.web.multipart.MultipartFile;

import com.abcm.jwt.entity.PronounciationHistory;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDTO {
    private String username;
 
     
    private LocalDate birthDate;
    private String gender;
    private String location;
    private String education;
    private String mobileNo;
    private String nativeLanguage;
    private MultipartFile profile;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PronounciationHistory> pronunciationHistories = new ArrayList<>();

    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public MultipartFile getProfile() {
		return profile;
	}
	public void setProfile(MultipartFile profile) {
		this.profile = profile;
	}
    
    

    // Getters and Setters
    // ...
}
