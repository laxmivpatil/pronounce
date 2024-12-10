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

import com.abcm.jwt.DTO.PronounciationHistoryDTO;
import com.abcm.jwt.DTO.PronounciationHistoryResponseDTO;
import com.abcm.jwt.entity.PronounciationHistory;
import com.abcm.jwt.entity.User;
import com.abcm.jwt.service.PronounciationHistoryService;
import com.abcm.jwt.service.UserService;

@RestController
@RequestMapping("/api/user/pronunciation-history")
public class PronounciationHistoryController {
	
	@Autowired
	private PronounciationHistoryService pronounciationHistoryService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/")
	public ResponseEntity<Map<String, Object>> savePronunciationHistory(@RequestHeader("Authorization") String jwt,@RequestBody PronounciationHistoryDTO historyDTO) {
		
		User user = userService.getUserFromToken(jwt);
		pronounciationHistoryService.savePronounciationHistory(user.getId(), historyDTO.getAccentId(),
	                                      historyDTO.getInputPronounce(), historyDTO.getOutputPronounce());
		  Map<String, Object> response = new HashMap<>();
		response.put("status", "success");
        response.put("message", "Pronunciation history saved successfully");
         return ResponseEntity.ok(response);
	     
	}
	
	 @GetMapping("/")
	    public ResponseEntity< Map<String, Object>> getHistoryByUserId(@RequestHeader("Authorization") String jwt) {
	        
	        
	    	User user = userService.getUserFromToken(jwt);
	    	  List<PronounciationHistoryResponseDTO> history = pronounciationHistoryService.getHistoryByUserId(user.getId());
		      
	    	Map<String, Object> response = new HashMap<>();
			response.put("status", "success");
	        response.put("message", "Pronunciation history  get successfully");
	        response.put("history", history);
	         return ResponseEntity.ok(response);
	         
	    }

	    // Get all history for a specific accent
	    @GetMapping("/accent/{accentId}")
	    public ResponseEntity<List<PronounciationHistory>> getHistoryByAccentId(@PathVariable Long accentId) {
	        List<PronounciationHistory> history = pronounciationHistoryService.getHistoryByAccentId(accentId);
	        return ResponseEntity.ok(history);
	    }


}
