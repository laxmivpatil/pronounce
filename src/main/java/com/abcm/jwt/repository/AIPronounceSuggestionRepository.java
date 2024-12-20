package com.abcm.jwt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.abcm.jwt.DTO.AIPronounceSuggestion;
import com.abcm.jwt.DTO.SuggestionProjection;

public interface AIPronounceSuggestionRepository extends JpaRepository<AIPronounceSuggestion, Long> {
	@Query("SELECT s.id AS id, s.suggestion AS suggestion FROM AIPronounceSuggestion s")
	List<SuggestionProjection> findAllSuggestions();

}
