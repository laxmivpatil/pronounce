package com.abcm.jwt.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.abcm.jwt.entity.MyUserDetails;
import com.abcm.jwt.entity.User;
import com.abcm.jwt.exception.UserNotFoundException;
import com.abcm.jwt.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	
	 
	@Override
	public UserDetails loadUserByUsername(String username) throws UserNotFoundException {

		 	Optional<User> user = this.userRepository.findByEmail(username);
		 	
			if (user.isEmpty())
				throw new UserNotFoundException("Could not find user");
			else
				return new MyUserDetails(user.get());

		 
	}

}
