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

	public Optional<UserModel> registerUser(UserModel userModel){

		if (userRepository.findByEmail(userModel.getEmail()).isPresent())
			return Optional.empty(); 
		
		userModel.setPassword(encryptPassword(userModel.getPassword()));

		return Optional.of(userRepository.save(userModel));

	}

	public Optional<UserModel> updateUser(UserModel userModel) {


		if(userRepository.findById(userModel.getId()).isPresent()) {

			Optional<UserModel> searchUser = userRepository.findByEmail(userModel.getEmail());

			if ( (searchUser.isPresent()) && ( searchUser.get().getId() != userModel.getId()))
				throw new ResponseStatusException(
						HttpStatus.BAD_REQUEST, "Usuário já existe!", null);

			userModel.setPassword(encryptPassword(userModel.getPassword()));

			return Optional.ofNullable(userRepository.save(userModel));

		}

			return Optional.empty();

	}

	public Optional<UserLogin> authenticateUser(Optional<UserLogin> userLogin){

		Optional<UserModel> userModel = userRepository.findByEmail(userLogin.get().getEmail()); 

		if(userModel.isPresent()) { 

			if(comparePassword(userLogin.get().getPassword(), userModel.get().getPassword())) { 
				userLogin.get().setId(userModel.get().getId());
				userLogin.get().setName(userModel.get().getName());
				userLogin.get().setPhoto(userModel.get().getPhoto());
				userLogin.get().setToken(generateBasicToken(userModel.get().getEmail(), userLogin.get().getPassword()));
				userLogin.get().setPassword(userModel.get().getPassword());

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

	private String generateBasicToken(String userModel, String password) {

		String token = userModel + ":" + password;
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII"))); 
		
		return "Basic " + new String(tokenBase64);

	}

}
