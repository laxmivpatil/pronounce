package com.abcm.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.abcm.jwt.DTO.AIChatSampleQuestions;
import com.abcm.jwt.service.AIChatSampleQuestionsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/samplequestions")
public class AIChatSampleQuestionsController {

    @Autowired
    private AIChatSampleQuestionsService service;

    // Endpoint to save a question
    @PostMapping
    public ResponseEntity<Map<String, Object>> saveQuestion(@RequestBody String question) {
        AIChatSampleQuestions savedQuestion = service.saveQuestion(question);

        // Create response as a Map
        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "question added successful");
        response.put("Question", savedQuestion);

        return ResponseEntity.ok(response);
    }
    
    
    @PostMapping("/list")
    public ResponseEntity<Map<String, Object>> saveQuestions(@RequestBody List<AIChatSampleQuestions> questions) {
        List<AIChatSampleQuestions> savedQuestions = new ArrayList<>();
        for (AIChatSampleQuestions question : questions) {
            savedQuestions.add(service.saveQuestion(question.getQuestion()));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "questions added successfully");
        response.put("Questions", savedQuestions);

        return ResponseEntity.ok(response);
    }


    // Endpoint to get all questions
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllQuestions() {
        List<AIChatSampleQuestions> questions = service.getAllQuestions();

        // Create response as a Map
        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "questions get successful");
        response.put("Questions", questions);

        return ResponseEntity.ok(response);
    }
}

