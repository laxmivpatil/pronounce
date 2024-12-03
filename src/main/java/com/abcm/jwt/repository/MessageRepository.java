package com.abcm.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abcm.jwt.entity.Message;
import com.abcm.jwt.entity.Prompt;

public interface MessageRepository extends JpaRepository<Message, Long> {
    // You can add custom query methods if needed
}

