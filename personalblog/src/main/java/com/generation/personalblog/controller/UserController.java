package com.generation.personalblog.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.personalblog.model.UserLogin;
import com.generation.personalblog.model.UserModel;
import com.generation.personalblog.repository.UserRepository;
import com.generation.personalblog.service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/all")
	public ResponseEntity<List<UserModel>>getAll(){
		
		return ResponseEntity.ok(userRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserModel>getById(@PathVariable Long id){
		
		return userRepository.findById(id).map(res-> ResponseEntity.ok(res))
				.orElse(ResponseEntity.notFound().build());
		
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<UserModel>> getByName(@PathVariable String name){
		
		return ResponseEntity.ok(userRepository.findAllByNameContainingIgnoreCase(name));
		
	}

	@PostMapping ("/login")
	public ResponseEntity<UserLogin> login(@RequestBody Optional<UserLogin> userLogin){
	
		return userService.authenticateUser(userLogin) 
					.map(res->ResponseEntity.status(HttpStatus.OK).body(res))
					.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
		
	}
	
	@PostMapping ("/register")
	public ResponseEntity<UserModel> register(@Valid @RequestBody UserModel userModel){
		
		return userService.registerUser(userModel)
					.map(res->ResponseEntity.status(HttpStatus.CREATED).body(res))
					.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
		
	}

	@PutMapping ("/update")
	public ResponseEntity<UserModel> updateUser(@Valid @RequestBody UserModel userModel){
			
		return userService.updateUser(userModel)
					.map(res->ResponseEntity.status(HttpStatus.OK).body(res))
					.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
}
