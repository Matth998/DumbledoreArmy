package com.generation.personalblog.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.generation.personalblog.model.UserModel;
import com.generation.personalblog.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<UserModel> userModel = repository.findByEmail(username);
		userModel.orElseThrow(() -> new UsernameNotFoundException(username + " not found."));
		
		return userModel.map(UserDetailsImpl::new).get();
	}

}
