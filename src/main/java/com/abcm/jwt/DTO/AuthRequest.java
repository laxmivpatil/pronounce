package com.abcm.jwt.DTO;
 

public class AuthRequest {
    private String username;
    private String email;
    private String password;
    private String authProvider; // "GOOGLE" or "LOCAL"
    private boolean login;
   
    
    
    
    
    
	public boolean isLogin() {
		return login;
	}
	public void setLogin(boolean login) {
		this.login = login;
	}
	public AuthRequest() {}	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthProvider() {
		return authProvider;
	}

	public void setAuthProvider(String authProvider) {
		this.authProvider = authProvider;
	}

	 
    
    
    

}