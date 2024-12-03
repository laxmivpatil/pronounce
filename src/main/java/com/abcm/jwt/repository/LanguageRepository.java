package com.abcm.jwt.repository;

 

import org.springframework.data.jpa.repository.JpaRepository;
import com.abcm.jwt.entity.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {
	  // Custom method to find a language by its name
    Language findByLanguageName(String languageName);
}
