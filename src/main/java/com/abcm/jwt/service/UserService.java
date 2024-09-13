package com.abcm.jwt.service;

 

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.abcm.jwt.DTO.UserDTO;
import com.abcm.jwt.entity.AppLanguage;
import com.abcm.jwt.entity.User;
import com.abcm.jwt.exception.UserNotFoundException;
import com.abcm.jwt.exception.UsernameAlreadyExistsException;
import com.abcm.jwt.repository.AppLanguageRepository;
import com.abcm.jwt.repository.UserRepository;
import com.abcm.jwt.security.JwtTokenHelper;

import io.jsonwebtoken.JwtException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AppLanguageRepository appLanguageRepository; 
    
    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    
    @Autowired
    private StorageService storageService;  
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private final String uploadDir = "F:\\MyProject\\JWT-Demo\\ProfileImg";

    public User save(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        	System.out.println("dsmbmsbv"+user.getEmail());
            throw new UsernameAlreadyExistsException("Email already exists: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    
    public User getUserFromToken(String token)  {
       
            String username = jwtTokenHelper.getUsernameFromToken(token.substring(7));
            return userRepository.findByEmail(username).get();
         
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, UserDTO userDTO) throws IOException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(userDTO.getUsername());
            user.setBirthDate(userDTO.getBirthDate());
            user.setGender(userDTO.getGender());
            user.setLocation(userDTO.getLocation());
            user.setEducation(userDTO.getEducation());
            user.setMobileNo(userDTO.getMobileNo());
            user.setNativeLanguage(userDTO.getNativeLanguage()); 

            MultipartFile profileFile = userDTO.getProfile();
            if (profileFile != null && !profileFile.isEmpty()) {
               String path=storageService.uploadFileOnAzure(profileFile);
            	//String path="https://satyaprofilestorage.blob.core.windows.net/pronouncestorage/1718437967912_430d1b68-63f2-40af-84ac-f63e7bed4809.jpg?sv=2021-04-10&st=2024-06-15T07%3A47%3A48Z&se=2099-01-01T00%3A00%3A00Z&sr=b&sp=r&sig=Xq9jS5wJJE1hsNXRG1ZkvENm8iqPoC1WUYzxX9iOwuQ%3D";
                user.setProfile(path);
            }
            else
            {
            	String path="https://satyaprofilestorage.blob.core.windows.net/pronouncestorage/1718437967912_430d1b68-63f2-40af-84ac-f63e7bed4809.jpg?sv=2021-04-10&st=2024-06-15T07%3A47%3A48Z&se=2099-01-01T00%3A00%3A00Z&sr=b&sp=r&sig=Xq9jS5wJJE1hsNXRG1ZkvENm8iqPoC1WUYzxX9iOwuQ%3D";
                user.setProfile(path);
            }

            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with id " + id);
        }
    }
    
    
    public void setAppLanguageForUser(Long userId, Long appLanguageId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        
        user.setAppLanguage(appLanguageRepository.findById(appLanguageId).get());
        userRepository.save(user);
    }

    
    public AppLanguage getUserAppLanguage(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        return user.getAppLanguage();
    }
    
    public Map<String, Object> changePassword(User user, String oldPassword, String newPassword) {
        
        Map<String, Object> response = new HashMap<>();

        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            response.put("status", true);
            response.put("message", "Password changed successfully");
        } else {
            response.put("status", false);
            response.put("message", "Old password is incorrect");
        }

        return response;
    }
    
 public Map<String, Object> forgetPassword( String email, String newPassword) {
        
        Map<String, Object> response = new HashMap<>();
        Optional<User> user=userRepository.findByEmail(email);
       if(user.isEmpty()) {
    	   response.put("status", true);
           response.put("message", "Email not exist");
       }
            user.get().setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user.get());
            response.put("status", true);
            response.put("message", "Password changed successfully");
         
        return response;
    }
}
