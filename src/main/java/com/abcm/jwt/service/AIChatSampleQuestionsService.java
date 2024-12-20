package com.abcm.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abcm.jwt.DTO.AIChatSampleQuestions;
import com.abcm.jwt.DTO.QuestionCache;
import com.abcm.jwt.DTO.SuggestionProjection;
import com.abcm.jwt.repository.AIChatSampleQuestionsRepository;

import java.util.List;

import javax.annotation.PostConstruct;

@Service
public class AIChatSampleQuestionsService {

    @Autowired
    private AIChatSampleQuestionsRepository repository;
    
    
    private List<QuestionCache> questionCache;

    @PostConstruct
    public void init() {
    	questionCache = repository.findAllQuestions();
    }

    public List<QuestionCache> getCachedQuestions() {
        return questionCache;
    }

    public AIChatSampleQuestions saveQuestion(String question) {
        return repository.save(new AIChatSampleQuestions(question));
    }

    public List<AIChatSampleQuestions> getAllQuestions() {
        return repository.findAll();
    }
}
