package com.abcm.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abcm.jwt.DTO.AIChatSampleQuestions;

public interface AIChatSampleQuestionsRepository extends JpaRepository<AIChatSampleQuestions, Long> {
}

