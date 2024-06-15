package com.abcm.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abcm.jwt.entity.User;
import com.abcm.jwt.exception.UserNotFoundException;
import com.abcm.jwt.exception.UsernameAlreadyExistsException;
import com.abcm.jwt.response.LoginResponse;
import com.abcm.jwt.security.JwtAuthRequest;
import com.abcm.jwt.security.JwtAuthResponse;
import com.abcm.jwt.security.JwtTokenHelper;
import com.abcm.jwt.service.UserService;

@RestController
@RequestMapping("api/v1/auth/")
public class AuthController {

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Autowired
	private UserDetailsService userDetailService;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;
	


	  @PostMapping("login")
	    public ResponseEntity<?> createToken(@RequestBody JwtAuthRequest authRequest) {
	         
	            

	            UserDetails userDetails = this.userDetailService.loadUserByUsername(authRequest.getEmail());
	            this.authenticate(authRequest.getEmail(), authRequest.getPassword());

	            String token = this.jwtTokenHelper.generateToken(userDetails);
	          //  System.out.println("hghghgh"+userDetails.getUsername());

	            LoginResponse response = new LoginResponse(true,"Login Successful",token);
	             
	            
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        
	    }

	    public void authenticate(String username, String password) throws BadCredentialsException,DisabledException {
	        try {
	            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
	            this.authenticationManager.authenticate(token);
	        } catch (DisabledException e) {
	            throw new DisabledException("USER_DISABLED", e);
	        } catch (BadCredentialsException e) {
	            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
	        }
	    }

	    @PostMapping("signup")
	    public ResponseEntity<?> registerUser(@RequestBody User user) {
	       
	            User savedUser = userService.save(user);
	            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	        
	    }
	     
	@GetMapping("test")
	public String test() {

		return "Test Call";
	}

}
