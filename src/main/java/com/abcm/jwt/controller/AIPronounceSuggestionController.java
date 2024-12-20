package com.abcm.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.abcm.jwt.DTO.AIPronounceSuggestion;
import com.abcm.jwt.DTO.SuggestionProjection;
import com.abcm.jwt.service.AIPronounceSuggestionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/suggestions")
public class AIPronounceSuggestionController {

    @Autowired
    private AIPronounceSuggestionService service;

    // Endpoint to save a single suggestion
    @PostMapping
    public ResponseEntity<Map<String, Object>> saveSuggestion(@RequestBody String suggestion) {
        AIPronounceSuggestion savedSuggestion = service.saveSuggestion(suggestion);

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Suggestion added successfully");
        response.put("Suggestion", savedSuggestion);

        return ResponseEntity.ok(response);
    }

    // Endpoint to save a list of suggestions
    @PostMapping("/list")
    public ResponseEntity<Map<String, Object>> saveSuggestions(@RequestBody List<AIPronounceSuggestion> suggestions) {
        List<AIPronounceSuggestion> savedSuggestions = service.saveSuggestions(suggestions);

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Suggestions added successfully");
        response.put("Suggestions", savedSuggestions);

        return ResponseEntity.ok(response);
    }

    // Endpoint to get all suggestions
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllSuggestions() {
        List<SuggestionProjection> suggestions = service.getCachedSuggestions();

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Suggestions retrieved successfully");
        response.put("Suggestions", suggestions);

        return ResponseEntity.ok(response);
    }

    // Endpoint to get a suggestion by ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getSuggestionById(@PathVariable Long id) {
        AIPronounceSuggestion suggestion = service.getSuggestionById(id);

        if (suggestion == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", false);
            response.put("message", "Suggestion not found");
            return ResponseEntity.status(404).body(response);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Suggestion retrieved successfully");
        response.put("Suggestion", suggestion);

        return ResponseEntity.ok(response);
    }
}

