package com.abcm.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.abcm.jwt.DTO.SetPriorityAccentsDTO;
import com.abcm.jwt.entity.Accent;
import com.abcm.jwt.entity.Language;
import com.abcm.jwt.entity.Message;
import com.abcm.jwt.entity.Prompt;
import com.abcm.jwt.entity.User;
import com.abcm.jwt.repository.AccentRepository;
import com.abcm.jwt.service.LanguageService;
import com.abcm.jwt.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accent")
public class LanguageController {

	@Autowired
	private LanguageService languageService;
	@Autowired
	private UserService userService;

	@Autowired
	private AccentRepository accentRepository;

	// API to add a language with accents
	@PostMapping("/add")
	public ResponseEntity<Object> addLanguageWithAccents(@RequestPart("countryName") String countryName,
			@RequestPart("languageName") String languageName, @RequestPart("accent") String accent, // Comma-separated
																									// list of accent
																									// names
			@RequestPart("flag") MultipartFile flag // Comma-separated list of flag URLs
	) {
		try {

			// Assuming that your `Accent` creation logic is handled by the service
			Accent accents = languageService.addLanguageWithAccents(countryName, languageName, accent, flag);

			// Save the Accent (with the static Prompt) to the database
			// languageService.saveAccent(accents);

			// Return the Accent with the static Prompt
			return ResponseEntity.ok(accents);

		} catch (Exception e) {
			return ResponseEntity.status(500).body("Failed to add language: " + e.getMessage());
		}
	}

	// API endpoint to set priority accents for a user
	@PutMapping("/setPriorityAccents")
	public ResponseEntity<Map<String, Object>> setPriorityAccents(
			@RequestBody SetPriorityAccentsDTO setPriorityAccentsDTO, @RequestHeader("Authorization") String token) {

		Map<String, Object> response = new HashMap<>();

		// Fetch user by ID
		User user = userService.getUserFromToken(token);

		// Fetch the accents by their IDs
		List<Long> accentIds = setPriorityAccentsDTO.getAccentIds();
		if (accentIds.size() != 3) {

			response.put("status", false);

			response.put("message", "Exactly 3 accents are required.");

			// Step 4: Return the response list as a ResponseEntity
			return ResponseEntity.ok(response);
			// return ResponseEntity.badRequest().body("Exactly 3 accents are required.");
		}

		// Validate that each accent ID exists
		List<Accent> accents = accentRepository.findAllById(accentIds);
		if (accents.size() != 3) {
			response.put("status", false);

			response.put("message", "One or more accent IDs are invalid.");

			// Step 4: Return the response list as a ResponseEntity
			return ResponseEntity.ok(response);

		}

		// Set the accents in the correct priority order
		user.setAccentIds(accentIds); // Set the accents in priority 1, 2, and 3

		// Save the updated user
		userService.savetodb(user);
		response.put("status", true);

		response.put("message", "Priority accents updated successfully.");

		// Step 4: Return the response list as a ResponseEntity
		return ResponseEntity.ok(response);

	}

	@GetMapping("/getp")
	public ResponseEntity<Map<String, Object>> getAllLanguagesWithAccentDetailsp(
			@RequestHeader("Authorization") String token) {
		try {

			// Fetch user by ID
			User user = userService.getUserFromToken(token);

			// Step 1: Get all languages from the service or repository
			List<Language> languages = languageService.getAllLanguages();

			// Step 2: Prepare the response list
			List<Map<String, Object>> responseList = new ArrayList<>();

			// Step 3: Iterate over each language and fetch the associated accent details
			for (Language language : languages) {
				// Step 3.1: Retrieve accent details for each language (assuming a language can
				// have multiple accents)
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
					systemMessage.put("content",
							"Your task is to rewrite the provided text in a conversational, phonetic "
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
			return ResponseEntity.status(500).body(
					Collections.singletonMap("error", "Failed to fetch languages with accents: " + e.getMessage()));
		}

	}

	@GetMapping("/get")
	public ResponseEntity<Map<String, Object>> getAllLanguagesWithAccentDetails(
			@RequestHeader("Authorization") String token) {
		try {
			// Fetch user from the token
			User user = userService.getUserFromToken(token);

			// Step 1: Get all languages and accents (Fetch all accents in one go)
			List<Language> languages = languageService.getAllLanguages();
			List<Accent> allAccents = accentRepository.findAll(); // Get all accents once

			// Create a map of accentId -> Accent for quick access
			Map<Long, Accent> accentIdToAccentMap = allAccents.stream()
					.collect(Collectors.toMap(Accent::getId, accent -> accent));

			// Step 2: Fetch user's priority accents (1, 2, 3)
			List<Long> userAccentIds = user.getAccentIds(); // Assuming this is a list of accent IDs for the user
			// Step 3: Prepare the response list
			List<Map<String, Object>> responseList = new ArrayList<>();

			// Step 4: Iterate over each language and fetch the associated accent details
			for (Language language : languages) {
				// Step 4.1: Retrieve all accents for the current language (instead of querying
				// each time)
				List<Accent> accentsForLanguage = allAccents.stream().filter(acc -> acc.getLanguage().equals(language))
						.collect(Collectors.toList());

				// Step 4.2: Process priority accents (1, 2, 3)
				for (int i = 0; i < userAccentIds.size() && i < 3; i++) {
					Long accentId = userAccentIds.get(i);

					Accent priorityAccent = accentIdToAccentMap.get(accentId);
					if (priorityAccent != null && accentsForLanguage.contains(priorityAccent)) {
						Map<String, Object> accentDetails = prepareAccentResponse(priorityAccent, i + 1);
						responseList.add(accentDetails);
					}
				}

				// Step 4.3: Process the remaining accents (those that are not assigned as
				// priority)
				for (Accent accent : accentsForLanguage) {
					if (!userAccentIds.contains(accent.getId())) {
						Map<String, Object> accentDetails = prepareAccentResponse(accent, 0); // For non-priority
						responseList.add(accentDetails);
					}
				}
			}

			// Step 5: Return the response list as a ResponseEntity
			Map<String, Object> response = new HashMap<>();
			response.put("listOfAccents", responseList);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			// Return an error response if something goes wrong
			return ResponseEntity.status(500).body(
					Collections.singletonMap("error", "Failed to fetch languages with accents: " + e.getMessage()));
		}
	}

	// Helper method to prepare accent response
	private Map<String, Object> prepareAccentResponse(Accent accent, int priority) {
	/*	Map<String, Object> prompt = new HashMap<>();
		prompt.put("model", "gpt-4");
		prompt.put("temperature", 0.7);

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
		
		*/

		// Create and return the response map
		Map<String, Object> accentDetails = new HashMap<>();
	 	accentDetails.put("languageName", accent.getLanguage().getLanguageName());
		accentDetails.put("countryName", accent.getCountryName());
		accentDetails.put("accentName", accent.getAccentName());
		accentDetails.put("flag", accent.getFlag());

		accentDetails.put("code", accent.getCode());
		accentDetails.put("maleVoice", accent.getMaleVoice());
		accentDetails.put("femaleVoice", accent.getFemaleVoice());
		//accentDetails.put("prompt", prompt);
		accentDetails.put("accentId", accent.getId());
		accentDetails.put("priority", priority);
 
accentDetails.put("code", accent.getCode());
		return accentDetails;
	}
	
	
	  @PutMapping("/{id}/update-gender-voice")
	    public ResponseEntity<?> updateGenderAndVoice(
	            @PathVariable Long id,
	            @RequestBody Map<String, String> request) {
	        try {
	            Accent accent = accentRepository.findById(id)
	                    .orElseThrow(() -> new RuntimeException("Accent not found"));

	            // Extract fields from JSON body
	            String malevoice = request.get("malevoice");
	            String femalevoice = request.get("femalevoice");

	          
	            accent.setMaleVoice(malevoice);
	            accent.setFemaleVoice(femalevoice);

	            accentRepository.save(accent);

	            return ResponseEntity.ok(accent);
	        } catch (Exception e) {
	            return ResponseEntity.status(500).body(e.getMessage());
	        }
	    }

}
