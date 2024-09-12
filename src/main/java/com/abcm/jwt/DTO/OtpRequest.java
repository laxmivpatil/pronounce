package com.abcm.jwt.DTO;

public class OtpRequest {
    private String email;
    private String otp;

    // Default constructor
    public OtpRequest() {
    }

    // Parameterized constructor
   
    
    // Getter and Setter for otp
    public String getOtp() {
        return otp;
    }

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setOtp(String otp) {
        this.otp = otp;
    }
}
