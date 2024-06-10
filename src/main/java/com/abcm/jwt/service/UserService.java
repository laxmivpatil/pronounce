package com.abcm.jwt.service;

 

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.abcm.jwt.entity.User;
import com.abcm.jwt.exception.UsernameAlreadyExistsException;
import com.abcm.jwt.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UsernameAlreadyExistsException("Email already exists: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
