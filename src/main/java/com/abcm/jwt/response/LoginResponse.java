package com.abcm.jwt.response;

public class LoginResponse {
	private boolean status;
    private String message;
    private String token;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public LoginResponse(boolean status, String message, String token) {
		super();
		this.status = status;
		this.message = message;
		this.token = token;
	}
	public LoginResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
    
}
