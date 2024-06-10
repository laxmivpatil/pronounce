package com.abcm.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abcm.jwt.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByUsername(String username);
	User findByEmail(String username);
}
