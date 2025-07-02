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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abcm.jwt.DTO.AuthRequest;
import com.abcm.jwt.DTO.GoogleLoginRequest;
import com.abcm.jwt.DTO.OtpRequest;
import com.abcm.jwt.entity.AuthProvider;
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
    private PasswordEncoder passwordEncoder;

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

/*
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
	     
	     
	     */
@PostMapping("signup")
public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User user) {
    Map<String, Object> response = new HashMap<>();

    // Check if user exists
    Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
    if (existingUser.isPresent()) {
        response.put("status", false);
        response.put("message", "User already exists.");
        return ResponseEntity.badRequest().body(response);
    }

    if (user.getAuthProvider() == AuthProvider.GOOGLE) {
        // Google Signup: No password required
        user.setPassword(null); // No password for Google Users
    } else {
        // Regular Signup: Hash the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    User savedUser = userRepository.save(user);
    UserDetails userDetails = this.userDetailService.loadUserByUsername(user.getEmail());
    String token = this.jwtTokenHelper.generateToken(userDetails);

    response.put("status", true);
    response.put("message", "Registration successful");
    response.put("token", token);
    response.put("user", savedUser);

    return ResponseEntity.ok(response);
}
@PostMapping("login")
public ResponseEntity<Map<String, Object>> createToken(@RequestBody JwtAuthRequest authRequest) {
    Map<String, Object> response = new HashMap<>();

    Optional<User> userOptional = userRepository.findByEmail(authRequest.getEmail());
    if (userOptional.isEmpty()) {
        response.put("status", false);
        response.put("message", "User not found");
        return ResponseEntity.badRequest().body(response);
    }

    User user = userOptional.get();

    if (user.getAuthProvider() == AuthProvider.GOOGLE) {
        // Google User: Allow login without password
        UserDetails userDetails = this.userDetailService.loadUserByUsername(user.getEmail());
        String token = this.jwtTokenHelper.generateToken(userDetails);

        response.put("status", true);
        response.put("message", "Google login successful");
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    // Manual User: Authenticate with password
    try {
        this.authenticate1(authRequest.getEmail(), authRequest.getPassword());
        UserDetails userDetails = this.userDetailService.loadUserByUsername(authRequest.getEmail());
        String token = this.jwtTokenHelper.generateToken(userDetails);

        response.put("status", true);
        response.put("message", "Login successful");
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("token", token);
        return ResponseEntity.ok(response);
    } catch (BadCredentialsException e) {
        response.put("status", false);
        response.put("message", "Invalid credentials");
        return ResponseEntity.badRequest().body(response);
    }
}


@PostMapping("/google-login")
public ResponseEntity<Map<String, Object>> googleLogin(@RequestBody GoogleLoginRequest request) {
    Map<String, Object> response = new HashMap<>();

    Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

    User user;
    if (existingUser.isPresent()) {
        user = existingUser.get();
    } else {
        // New Google User: Register
        user = new User();
        user.setUsername(request.getName());
        user.setEmail(request.getEmail());
        user.setProfile(request.getPhotoUrl());
        user.setAuthProvider(AuthProvider.GOOGLE);
        userRepository.save(user);
    }

    UserDetails userDetails = this.userDetailService.loadUserByUsername(user.getEmail());
    String token = this.jwtTokenHelper.generateToken(userDetails);

    response.put("status", true);
    response.put("message", "Google login successful");
    response.put("username", user.getUsername());
    response.put("email", user.getEmail());
    response.put("token", token);

    return ResponseEntity.ok(response);
}

public void authenticate1(String username, String password) throws BadCredentialsException,DisabledException {
    try {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        this.authenticationManager.authenticate(token);
    } catch (DisabledException e) {
        throw new DisabledException("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
        throw new BadCredentialsException("INVALID_CREDENTIALS", e);
    }
}


	   
//////here



@PostMapping("login-signup")
public ResponseEntity<Map<String, Object>> authenticateOrRegister(@RequestBody AuthRequest request) {
    Map<String, Object> response = new HashMap<>();

    
    Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
    System.out.println(existingUser.toString()+ existingUser.isEmpty() + request.toString());
    User user;
    if (existingUser.isEmpty() && request.isLogin()) {
    	System.out.println("not registered user");
    	 response.put("message", "email not registered please registered first");
         response.put("status", false);        
         return ResponseEntity.ok(response);	
    }
    else if (existingUser.isPresent() && !request.isLogin()) {
    	System.out.println("allready registered user please login");
    	 response.put("message", "email allready registered please login");
         response.put("status", false);        
         return ResponseEntity.ok(response);	
    }
    else if (existingUser.isPresent() && request.isLogin()) {
        user = existingUser.get();

        //  If user is Google registered, login without password
        if (user.getAuthProvider() == AuthProvider.GOOGLE) {
            UserDetails userDetails = this.userDetailService.loadUserByUsername(user.getEmail());
            String token = this.jwtTokenHelper.generateToken(userDetails);

            response.put("status", true);
            response.put("message", "User allready registered , Google login successful");
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("token", token);
            return ResponseEntity.ok(response);
        }
        //  If user is facebook registered, login without password
        if (user.getAuthProvider() == AuthProvider.FACEBOOK) {
            UserDetails userDetails = this.userDetailService.loadUserByUsername(user.getEmail());
            String token = this.jwtTokenHelper.generateToken(userDetails);

            response.put("status", true);
            response.put("message", "User allready registered , Facebook login successful");
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("token", token);
            return ResponseEntity.ok(response);
        }

        // 🔐 If user is LOCAL, verify password
        try {
            this.authenticate(request.getEmail(), request.getPassword());
            UserDetails userDetails = this.userDetailService.loadUserByUsername(request.getEmail());
            String token = this.jwtTokenHelper.generateToken(userDetails);

            response.put("status", true);
            response.put("message", "User allready registered , Login successful");
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            response.put("status", false);
            response.put("message", "Invalid credentials");
            return ResponseEntity.ok().body(response);
        }
    } else {
        // 🆕 If User is new, register (Google or Local)
        user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        
        if (request.getAuthProvider().equalsIgnoreCase("GOOGLE")) {
            user.setAuthProvider(AuthProvider.GOOGLE);
            user.setPassword(null);
        } 
        else if (request.getAuthProvider().equalsIgnoreCase("FACEBOOK")) {
            user.setAuthProvider(AuthProvider.FACEBOOK);
            user.setPassword(null);
        }  
        else {
            user.setAuthProvider(AuthProvider.LOCAL);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);

        UserDetails userDetails = this.userDetailService.loadUserByUsername(user.getEmail());
        String token = this.jwtTokenHelper.generateToken(userDetails);

        response.put("status", true);
        response.put("message", "User registered successfully");
        //response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("token", token);
        
        return ResponseEntity.ok(response);
    }
    
}

public void authenticate(String email, String password) throws BadCredentialsException, DisabledException {
    try {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
        this.authenticationManager.authenticate(token);
    } catch (DisabledException e) {
        throw new DisabledException("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
        throw new BadCredentialsException("INVALID_CREDENTIALS", e);
    }
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
