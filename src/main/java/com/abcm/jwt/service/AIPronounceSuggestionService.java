package com.abcm.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.abcm.jwt.DTO.AIPronounceSuggestion;
import com.abcm.jwt.DTO.SuggestionProjection;
import com.abcm.jwt.repository.AIPronounceSuggestionRepository;

import java.util.List;

import javax.annotation.PostConstruct;

@Service
public class AIPronounceSuggestionService {

    @Autowired
    private AIPronounceSuggestionRepository repository;
    
    
    private List<SuggestionProjection> cachedSuggestions;

    @PostConstruct
    public void init() {
        cachedSuggestions = repository.findAllSuggestions();
    }

    public List<SuggestionProjection> getCachedSuggestions() {
        return cachedSuggestions;
    }

    public AIPronounceSuggestion saveSuggestion(String suggestion) {
        return repository.save(new AIPronounceSuggestion(suggestion));
    }

    public List<AIPronounceSuggestion> saveSuggestions(List<AIPronounceSuggestion> suggestions) {
        return repository.saveAll(suggestions);
    }
    
    
    
    public AIPronounceSuggestion getSuggestionById(Long id) {
        return repository.findById(id).orElse(null);
    }
}

