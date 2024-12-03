package com.abcm.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.abcm.jwt.entity.Accent;
import com.abcm.jwt.entity.Language;
import com.abcm.jwt.entity.Message;
import com.abcm.jwt.entity.Prompt;
import com.abcm.jwt.repository.AccentRepository;
import com.abcm.jwt.repository.LanguageRepository;
import com.abcm.jwt.repository.PromptRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class LanguageService {

	@Autowired
	private LanguageRepository languageRepository;
	
	@Autowired
	private PromptRepository promptRepository;

	@Autowired
	private StorageService storageService;

	@Autowired
	private AccentRepository accentRepository;

	// Add a new language with accents, ensuring the language is unique
	public Accent addLanguageWithAccents(String countryName, String languageName, String accentName, MultipartFile flag) {
	    // Check if the language already exists
	    Language language = languageRepository.findByLanguageName(languageName);
	    
	    String path = storageService.uploadFileOnAzure(flag);

	    if (language == null) {
	        // If the language doesn't exist, create a new one
	        language = new Language();
	        language.setLanguageName(languageName);
	        language = languageRepository.save(language);
	    }

	    // Create the static Prompt
	    Prompt staticPrompt = new Prompt();
	    staticPrompt.setModel("gpt-4");
	    staticPrompt.setTemperature(0.7);

	    // Create messages for the prompt
	    Message systemMessage = new Message();
	    systemMessage.setRole("system");
	    systemMessage.setContent("Your task is to rewrite the provided text in a conversational, phonetic " + accentName + " " + languageName + " accent while retaining all original words. Do not drop or add words. Use informal spellings and contractions to mimic casual " + accentName + " speech.");

	    Message userMessage = new Message();
	    userMessage.setRole("user");
	    userMessage.setContent("Rewrite the following text in a " + accentName + " " + languageName + " accent");

	    // Add the messages to the prompt
	    List<Message> messages = new ArrayList<>();
	    messages.add(systemMessage);
	    messages.add(userMessage);
	    staticPrompt.setMessages(messages);

	    // Save the Prompt first
	    staticPrompt = promptRepository.save(staticPrompt); // Explicitly save the prompt

	    // Create the Accent
	    Accent accent = new Accent();
	    accent.setCountryName(countryName);
	    accent.setAccentName(accentName);
	    accent.setFlag(path);
	    accent.setLanguage(language);  // Link the accent to the existing language
	    accent.setPrompt(staticPrompt);  // Set the prompt for the accent

	    // Save the Accent
	    accentRepository.save(accent);
	    return accent;
	}

	// Get all languages and their accents
	public List<Language> getAllLanguages() {
		return languageRepository.findAll();
	}
}
