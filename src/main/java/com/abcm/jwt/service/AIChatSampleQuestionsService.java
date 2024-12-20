package com.abcm.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abcm.jwt.DTO.AIChatSampleQuestions;
import com.abcm.jwt.repository.AIChatSampleQuestionsRepository;

import java.util.List;

@Service
public class AIChatSampleQuestionsService {

    @Autowired
    private AIChatSampleQuestionsRepository repository;

    public AIChatSampleQuestions saveQuestion(String question) {
        return repository.save(new AIChatSampleQuestions(question));
    }

    public List<AIChatSampleQuestions> getAllQuestions() {
        return repository.findAll();
    }
}
