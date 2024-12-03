package com.abcm.jwt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.abcm.jwt.entity.Accent;
import com.abcm.jwt.entity.Language;

public interface AccentRepository extends JpaRepository<Accent, Long> {
	   List<Accent> findByLanguage(Language language);
}
