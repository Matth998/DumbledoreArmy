package com.generation.personalblog.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.personalblog.model.UserLogin;
import com.generation.personalblog.model.UserModel;
import com.generation.personalblog.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public Optional<UserModel> registerUser(UserModel user){
		
		if (userRepository.findByUser(user.getUser()).isPresent())
			return Optional.empty();
		
		user.setPassword(encryptPassword(user.getPassword()));
		
		return Optional.of(userRepository.save(user));
		
	}
	
	public Optional<UserModel> updateUser(UserModel user) {

		
		if(userRepository.findById(user.getId()).isPresent()) {
			
			Optional<UserModel> searchUser = userRepository.findByUser(user.getUser());
			
			if ( (searchUser.isPresent()) && ( searchUser.get().getId() != user.getId()))
				throw new ResponseStatusException(
						HttpStatus.BAD_REQUEST, "Usuário já existe!", null);
			
			user.setPassword(encryptPassword(user.getPassword()));

			return Optional.ofNullable(userRepository.save(user));
			
		}
		
			return Optional.empty();
	
	}
	
	public Optional<UserLogin> authenticateUser(Optional<UserLogin> userLogin){
		
		Optional<UserModel> user = userRepository.findByUser(userLogin.get().getUser());
		
		if(user.isPresent()) {
			
			if(comparePassword(userLogin.get().getPassword(), user.get().getPassword())) {
				
				userLogin.get().setId(user.get().getId());
				userLogin.get().setName(user.get().getName());
				userLogin.get().setPhoto(user.get().getPhoto());
				userLogin.get().setToken(generateBasicToken(user.get().getUser(), userLogin.get().getPassword()));
				userLogin.get().setPassword(user.get().getPassword());
				
				return userLogin;
			}
		}
		
		return Optional.empty();
	}
	
	private String encryptPassword(String password) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.encode(password);
	}
	
	private boolean comparePassword(String typedPassword, String dbPassword) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.matches(typedPassword, dbPassword);
		
	}
	
	private String generateBasicToken(String user, String password) {
		
		String token = user + ":" + password;
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		return "Basic " + new String(tokenBase64);
		
	}
	
}
