package com.example.vmedico.service;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vmedico.entity.User;
import com.example.vmedico.repository.UserRepository;
@Service("userService")
public class UserService {
	private UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public User findByConfirmationToken(String confirmationToken) {
		return userRepository.findByConfirmationToken(confirmationToken);
	}
	
	public void saveUser(User user) {
		userRepository.save(user);
	}
	
	public List<User> findAll() {
		
		return userRepository.findAll();
	}

}
