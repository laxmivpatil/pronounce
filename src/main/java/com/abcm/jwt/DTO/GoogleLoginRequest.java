package com.abcm.jwt.DTO;
 

public class GoogleLoginRequest {

    private String name;
    private String email;
    private String photoUrl;

    // Constructors
    public GoogleLoginRequest() {}

    public GoogleLoginRequest(String name, String email, String photoUrl) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
