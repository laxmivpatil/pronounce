package com.abcm.jwt.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abcm.jwt.DTO.OtpRequest;
import com.abcm.jwt.entity.User;
import com.abcm.jwt.exception.UserNotFoundException;
import com.abcm.jwt.exception.UsernameAlreadyExistsException;
import com.abcm.jwt.repository.UserRepository;
import com.abcm.jwt.response.LoginResponse;
import com.abcm.jwt.security.JwtAuthRequest;
import com.abcm.jwt.security.JwtAuthResponse;
import com.abcm.jwt.security.JwtTokenHelper;
import com.abcm.jwt.service.OtpService;
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
	private OtpService otpService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository  userRepository;
	

	
	
	
	 @GetMapping("/generateotp")
	    public ResponseEntity< Map<String, Object>> generateOtp(@RequestParam String email) {
		 System.out.println("otp hiii ");
	        String otp = otpService.generateOtp(email);
	        System.out.println("otp "+otp);
	        otpService.sendOtp(otp, email);
	        
	        
	        Map<String, Object> response = new HashMap<>();
	        response.put("status", true);
	        response.put("message", "OTP has been sent!");
	        response.put("otp", otp);
	        return ResponseEntity.ok(response);

	         
	    }

	
	@PostMapping("/validateotp")
	public ResponseEntity<Map<String, Object> > validateOtp(@RequestBody OtpRequest otpRequest) {
	    boolean isValid = otpService.validateOtp(otpRequest.getEmail(), otpRequest.getOtp());
	    Map<String, Object> response = new HashMap<>();
	    if (isValid) {
	        response.put("status", true);
	        response.put("message", "OTP is valid.");
	        return ResponseEntity.ok(response);
	    } else {
	        response.put("status", false);
	        response.put("message", "Invalid or expired OTP.");
	        return ResponseEntity.status(HttpStatus.OK).body(response);
	    }
	}
	
	
	
	@GetMapping("/forget/generateotp")
    public ResponseEntity< Map<String, Object>> forgetgenerateOtp(@RequestParam String email) {
		 Map<String, Object> response = new HashMap<>();
		 Optional<User> user= userRepository.findByEmail(email);
		 if(user.isEmpty())
		 {
		       
		        response.put("status", false);
		        response.put("message", "email not registered");
		        return ResponseEntity.ok(response);
		 }
	 
        String otp = otpService.generateOtp(email);
         
        otpService.sendOtp(otp, email);
        
        
       
        response.put("status", true);
        response.put("message", "OTP has been sent!");
        response.put("otp", otp);
        return ResponseEntity.ok(response);

         
    }


@PostMapping("/forget/validateotp")
public ResponseEntity<Map<String, Object> > forgetvalidateOtp(@RequestBody OtpRequest otpRequest) {
    boolean isValid = otpService.validateOtp(otpRequest.getEmail(), otpRequest.getOtp());
    Map<String, Object> response = new HashMap<>();
    if (isValid) {
    	 UserDetails userDetails = this.userDetailService.loadUserByUsername(otpRequest.getEmail());
         
     

         String token = this.jwtTokenHelper.generateToken(userDetails);
         User user= userRepository.findByEmail(otpRequest.getEmail()).get();
	        response.put("username", user.getUsername());
	        response.put("email", user.getEmail());
	        response.put("token", token);
	        response.put("status", true);
        response.put("profile", user.getProfile());
        response.put("message", "OTP is valid.");
        return ResponseEntity.ok(response);
    } else {
        response.put("status", false);
        response.put("message", "Invalid or expired OTP.");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
	  @PostMapping("login")
	    public ResponseEntity< Map<String, Object>> createToken(@RequestBody JwtAuthRequest authRequest) {
		        UserDetails userDetails = this.userDetailService.loadUserByUsername(authRequest.getEmail());
	            
	            this.authenticate(authRequest.getEmail(), authRequest.getPassword());

	            String token = this.jwtTokenHelper.generateToken(userDetails);
	           
	            Map<String, Object> response = new HashMap<>();
	           
	           User user= userRepository.findByEmail(authRequest.getEmail()).get();
	          
	            response.put("status", true);
		        response.put("message", "Login Successful");
		        response.put("username", user.getUsername());
		        response.put("email", user.getEmail());
		        response.put("token", token);
		        return ResponseEntity.ok(response);
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
	    public ResponseEntity<Map<String, Object>>  registerUser(@RequestBody User user) {
	    	 Map<String, Object> response = new HashMap<>();
	            User savedUser = userService.save(user);
	            UserDetails userDetails = this.userDetailService.loadUserByUsername(user.getEmail());
	            String token = this.jwtTokenHelper.generateToken(userDetails);
	            if(savedUser!=null) {
	            	 response.put("status", true);
	                 response.put("message", "Registration successfully done");
	                 response.put("token", token);
	                 response.put("user", savedUser);
	                
	            } 
	            else {
	            	 response.put("status", false);
	                 response.put("message", "Registration not done error occurs");
	                 
	            }
	            return ResponseEntity.ok(response);
	    }
	     
	    
	    @PostMapping("/forget-password")
	    public ResponseEntity<Map<String, Object>> forgetPassword(
	             
	            @RequestBody Map<String, Object> request) {
	    	  
	        String email= (String) request.get("email");
	        String newPassword = (String) request.get("newPassword");

	        Map<String, Object> response = userService.forgetPassword(email ,newPassword);

	        return ResponseEntity.ok(response);
	    }
	@GetMapping("test")
	public String test() {

		return "Test Call";
	}

}
