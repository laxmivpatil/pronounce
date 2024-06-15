package com.abcm.jwt.repository;
 

 
import org.springframework.data.jpa.repository.JpaRepository;

import com.abcm.jwt.entity.AppLanguage;

public interface AppLanguageRepository extends JpaRepository<AppLanguage, Long> {
}
