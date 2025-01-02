package com.abcm.jwt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.abcm.jwt.DTO.AIChatSampleQuestions;
import com.abcm.jwt.DTO.QuestionCache;
import com.abcm.jwt.DTO.SuggestionProjection;

public interface AIChatSampleQuestionsRepository extends JpaRepository<AIChatSampleQuestions, Long> {
	
	
	@Query("SELECT s.id AS id, s.question AS question FROM AIChatSampleQuestions s")
	List<QuestionCache> findAllQuestions();

}                                                                                 
                
