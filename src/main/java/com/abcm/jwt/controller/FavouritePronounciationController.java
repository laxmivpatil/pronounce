package com.abcm.jwt.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abcm.jwt.DTO.PronounciationHistoryResponseDTO;
import com.abcm.jwt.entity.PronounciationHistory;
import com.abcm.jwt.entity.User;
import com.abcm.jwt.service.PronounciationHistoryService;
import com.abcm.jwt.service.UserService;

@RestController
@RequestMapping("/api/user/pronunciation/favorite")
public class FavouritePronounciationController {

	
	 
	    @Autowired
	    private PronounciationHistoryService service;

	    @Autowired
	    private UserService userService;

	    @PostMapping("/add/{pronounceId}")
	    public ResponseEntity<  Map<String, Object> > addToFavorites(@PathVariable Long pronounceId,
	                                                                @RequestHeader("Authorization") String token) {
	       User user = userService.getUserFromToken(token);
	       PronounciationHistoryResponseDTO history = service.markAsFavorite(pronounceId, user.getId());
	        
	       
	       
	       Map<String, Object> response = new HashMap<>();
			response.put("status", "success");
	        response.put("message", "Pronunciation added to favorite successfully");
	        response.put("favorite", history);
	         return ResponseEntity.ok(response);
	         
	         
	    }
	    
	    @PostMapping("/update-status")
	    public ResponseEntity<Map<String, Object>> updateFavoriteStatus(
	            @RequestBody Map<String, Object> requestBody,
	            @RequestHeader("Authorization") String token) {

	        // Extract the data from the request body
	        Long pronounceId = ((Number) requestBody.get("id")).longValue();
	        Boolean status = (Boolean) requestBody.get("status");

	        // Extract user information from the token
	        User user = userService.getUserFromToken(token);

	        // Call the service to update the status
	        PronounciationHistoryResponseDTO updatedHistory = service.updateFavoriteStatus(
	                pronounceId,
	                user.getId(),
	                status
	        );

	        // Prepare the response
	        Map<String, Object> response = new HashMap<>();
	        response.put("status", "success");
	        response.put("message", "Pronunciation status updated successfully");
	        response.put("updatedFavorite", updatedHistory);

	        return ResponseEntity.ok(response);
	    }


	    @GetMapping("/get")
	    public ResponseEntity<  Map<String, Object> > getFavorites(@RequestHeader("Authorization") String token) {
	    	  User user = userService.getUserFromToken(token);
	 	      List<PronounciationHistoryResponseDTO> favorites = service.getFavoritesByUser(user.getId());
	        
	        
	 	     Map<String, Object> response = new HashMap<>();
				response.put("status", "success");
		        response.put("message", "Pronunciation get favorite successfully");
		        response.put("favorite", favorites);
		         return ResponseEntity.ok(response);
		         
	    }
	}

	
	
	