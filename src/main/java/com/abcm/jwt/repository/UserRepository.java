package com.abcm.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abcm.jwt.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);
	 Optional<User> findByEmail(String email);
}
