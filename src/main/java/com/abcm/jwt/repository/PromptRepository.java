 package com.abcm.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abcm.jwt.entity.Prompt;

public interface PromptRepository extends JpaRepository<Prompt, Long> {
    // You can add custom query methods if needed
}
