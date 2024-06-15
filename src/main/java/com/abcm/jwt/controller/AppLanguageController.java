package com.abcm.jwt.controller;

 
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.abcm.jwt.entity.AppLanguage;
import com.abcm.jwt.service.AppLanguageService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/languages")
public class AppLanguageController {
    @Autowired
    private AppLanguageService languageService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllLanguages() {
        List<AppLanguage> languages = languageService.getAllLanguages();
        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Languages fetched successfully");
        response.put("data", languages);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getLanguageById(@PathVariable Long id) {
        return languageService.getLanguageById(id)
                .map(language -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", true);
                    response.put("message", "Language found");
                    response.put("data", language);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", false);
                    response.put("message", "Language not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createLanguage(
            @RequestParam("language") String language,
            @RequestPart("logo") MultipartFile logo) {
    	
        AppLanguage createdLanguage = languageService.saveLanguage(language, logo);
        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Language created successfully");
        response.put("data", createdLanguage);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateLanguage(
            @PathVariable Long id, 
            @RequestBody AppLanguage languageDetails) {
        
        return languageService.getLanguageById(id)
                .map(language -> {
                    language.setLanguageName(languageDetails.getLanguageName());
                    language.setLogoUrl(languageDetails.getLogoUrl());
                    AppLanguage updatedLanguage = languageService.saveLanguage1(language);
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", true);
                    response.put("message", "Language updated successfully");
                    response.put("data", updatedLanguage);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", false);
                    response.put("message", "Language not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteLanguage(@PathVariable Long id) {
        languageService.deleteLanguage(id);
        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Language deleted successfully");
        return ResponseEntity.ok(response);
    }
}
