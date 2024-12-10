package com.abcm.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.abcm.jwt.entity.Accent;
import com.abcm.jwt.entity.Language;
import com.abcm.jwt.entity.Message;
import com.abcm.jwt.entity.Prompt;
import com.abcm.jwt.repository.AccentRepository;
import com.abcm.jwt.service.LanguageService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accent")
public class LanguageController {

    @Autowired
    private LanguageService languageService;
    
    @Autowired 
    private AccentRepository accentRepository;
    

    // API to add a language with accents
    @PostMapping("/add")
    public ResponseEntity<Object> addLanguageWithAccents(
            @RequestPart("countryName") String countryName,
            @RequestPart("languageName") String languageName,
            @RequestPart("accent") String accent, // Comma-separated list of accent names
            @RequestPart("flag") MultipartFile flag // Comma-separated list of flag URLs
    ) {
        try {
           

            // Assuming that your `Accent` creation logic is handled by the service
            Accent accents = languageService.addLanguageWithAccents(countryName,languageName, accent, flag);

                       // Save the Accent (with the static Prompt) to the database
            // languageService.saveAccent(accents);

            // Return the Accent with the static Prompt
            return ResponseEntity.ok(accents);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to add language: " + e.getMessage());
        }
    }


    @GetMapping("/get")
    public ResponseEntity<Map<String, Object>> getAllLanguagesWithAccentDetails() {
        try {
            // Step 1: Get all languages from the service or repository
            List<Language> languages = languageService.getAllLanguages();

            // Step 2: Prepare the response list
            List<Map<String, Object>> responseList = new ArrayList<>();

            // Step 3: Iterate over each language and fetch the associated accent details
            for (Language language : languages) {
                // Step 3.1: Retrieve accent details for each language (assuming a language can have multiple accents)
                List<Accent> accents = accentRepository.findByLanguage(language);

                // Step 3.2: Process each accent for the language
                for (Accent accent : accents) {
                    // Create the prompt for this specific accent
                    Map<String, Object> prompt = new HashMap<>();
                    prompt.put("model", "gpt-4");
                    prompt.put("temperature", 0.7);

                    // Create the list of messages
                    List<Map<String, String>> messages = new ArrayList<>();

                    Map<String, String> systemMessage = new HashMap<>();
                    systemMessage.put("role", "system");
                    systemMessage.put("content", "Your task is to rewrite the provided text in a conversational, phonetic " 
                            + accent.getAccentName() + " " + accent.getLanguage().getLanguageName() 
                            + " accent while retaining all original words. Do not drop or add words. Use informal spellings and contractions to mimic casual "
                            + accent.getAccentName() + " speech.");

                    Map<String, String> userMessage = new HashMap<>();
                    userMessage.put("role", "user");
                    userMessage.put("content", "Rewrite the following text in a " + accent.getAccentName() + " " 
                            + accent.getLanguage().getLanguageName() + " accent");

                    messages.add(systemMessage);
                    messages.add(userMessage);

                    prompt.put("messages", messages);

                    // Step 3.3: Construct the response map for this specific language and accent
                    Map<String, Object> languageWithAccent = new HashMap<>();
                    languageWithAccent.put("languageName", accent.getLanguage().getLanguageName());
                    languageWithAccent.put("countryName", accent.getCountryName());
                    languageWithAccent.put("accentName", accent.getAccentName());
                    languageWithAccent.put("flag", accent.getFlag());
                    languageWithAccent.put("code", accent.getCode());
                    languageWithAccent.put("prompt", prompt);
                    languageWithAccent.put("accentId", accent.getId());
                    
                    // Step 3.4: Add this language and accent to the response list
                    responseList.add(languageWithAccent);
                }
            }
            Map<String, Object> response = new HashMap<>();
            response.put("listOfAccents", responseList);
            // Step 4: Return the response list as a ResponseEntity
            return ResponseEntity.ok(response);
        } catch (Exception e) {
                // Return an error response if something goes wrong
                return ResponseEntity.status(500).body(Collections.singletonMap("error", "Failed to fetch languages with accents: " + e.getMessage()));
            }
        
    }


}
