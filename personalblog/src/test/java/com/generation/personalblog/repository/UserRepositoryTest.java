package com.generation.personalblog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.generation.personalblog.model.UserModel;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@BeforeAll
	void start() {
		
		userRepository.save(new UserModel(0L, "Matheus da Silva", "matheusoliveira.3125@gmail.com", "123456789", "https://i.imgur.com/mhO83HD.png"));
		userRepository.save(new UserModel(0L, "Luiz Igor", "luiz.igor21@gmail.com", "123456789", "https://i.imgur.com/mhO83HD.png"));
		userRepository.save(new UserModel(0L, "Marcos Alves", "marcolaalves.222@gmail.com", "123456789", "https://i.imgur.com/mhO83HD.png"));
		userRepository.save(new UserModel(0L, "Igor Luiz", "luiz.carecabrabo@gmail.com", "123456789", "https://i.imgur.com/mhO83HD.png"));
		userRepository.save(new UserModel(0L, "Robson Japones", "robsondosjapones@gmail.com", "123456789", "https://i.imgur.com/mhO83HD.png"));
		userRepository.save(new UserModel(0L, "Luiz Paulo", "Luiz.paulo@gmail.com", "123456789", "https://i.imgur.com/mhO83HD.png"));
		
	}
	
	@Test
	@DisplayName("Returns only one user")
	public void returnOneUser() {
		
		Optional<UserModel> userModel = userRepository.findByUser("luiz.carecabrabo@gmail.com");
		
		assertTrue(userModel.get().getUser().equals("luiz.carecabrabo@gmail.com"));
		
	}
	
	@Test
	@DisplayName("Returns three users")
	public void returnThreeUser() {
		
		List<UserModel> listUser = userRepository.findAllByNameContainingIgnoreCase("Luiz");
		
		assertEquals(3, listUser.size());
		
		assertTrue(listUser.get(0).getName().equals("Luiz Igor"));
		
		assertTrue(listUser.get(1).getName().equals("Igor Luiz"));
		
		assertTrue(listUser.get(2).getName().equals("Luiz Paulo"));
	}
	
	
	@AfterAll
	public void end() {
		
		userRepository.deleteAll();
		
	}
	
}
