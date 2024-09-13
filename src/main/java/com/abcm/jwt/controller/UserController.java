 
package com.abcm.jwt.controller;

import com.abcm.jwt.DTO.UserDTO;
import com.abcm.jwt.entity.User;
import com.abcm.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getUserDetails(@RequestHeader("Authorization") String token) {
    	Map<String, Object> response = new HashMap<>();
    	try {
            // Extract token (bearer token)
           
            
            // Get username from token
           User user = userService.getUserFromToken(token);

            

           response.put("status", true);
           response.put("message", "User retrived successfully");
           response.put("user", user);
           return ResponseEntity.ok(response);
        } catch (Exception e) {
        	e.printStackTrace();
        	  response.put("status",false);
              response.put("message", "Invalid token");
              return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/")
    public ResponseEntity<Map<String, Object>> updateUser(@RequestHeader("Authorization") String jwt,
                                                          @RequestParam("username") String username,
                                                          @RequestParam("birthDate") String birthDate,
                                                          @RequestParam("gender") String gender,
                                                          @RequestParam("location") String location,
                                                          @RequestParam("education") String education,
                                                          @RequestParam("mobileNo") String mobileNo,
                                                          @RequestParam("nativeLanguage") String nativeLanguage,
                                                          @RequestPart(required=false,value="profile") MultipartFile profile) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
         
        userDTO.setBirthDate(LocalDate.parse(birthDate));
        userDTO.setGender(gender);
        userDTO.setLocation(location);
        userDTO.setEducation(education);
        userDTO.setMobileNo(mobileNo);
        userDTO.setNativeLanguage(nativeLanguage);
        userDTO.setProfile(profile);

        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.getUserFromToken(jwt);
            User updatedUser = userService.updateUser(user.getId(), userDTO);
            System.out.println(updatedUser);
            response.put("status", "success");
            response.put("message", "User updated successfully");
            response.put("user", updatedUser);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
        	e.printStackTrace();
            response.put("status", "error");
            response.put("message", "Error processing the profile image");
            return ResponseEntity.status(500).body(response);
        } catch (RuntimeException e) {
        	e.printStackTrace();
            response.put("status", "error");
            response.put("message", "User not found");
            return ResponseEntity.status(404).body(response);
        }catch (Exception e) {
        	e.printStackTrace();
            response.put("status", "error");
            response.put("message", "Error processing the profile image");
            return ResponseEntity.status(500).body(response);
        } 
    }
    
    
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(
            @RequestHeader("Authorization") String jwt, 
            @RequestBody Map<String, Object> request) {
    	  User user = userService.getUserFromToken(jwt);
        String oldPassword = (String) request.get("oldPassword");
        String newPassword = (String) request.get("newPassword");

        Map<String, Object> response = userService.changePassword(user, oldPassword, newPassword);

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
    
}
