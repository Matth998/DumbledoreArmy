package com.generation.personalblog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.personalblog.model.PostModel;
import com.generation.personalblog.repository.PostRepository;

@RestController
@RequestMapping("/post")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostController {

	@Autowired
	private PostRepository repository;
	
	@GetMapping
	public ResponseEntity<List<PostModel>> GetAll(){
		
		return ResponseEntity.ok(repository.findAll());
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PostModel> GetById(@PathVariable long id){
		
		return repository.findById(id).map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
		
	}
	
	@GetMapping("/title/{title}")
	public ResponseEntity<List<PostModel>> GetByTitle(@PathVariable String title){
		
		return ResponseEntity.ok(repository.findAllByTitleContainingIgnoreCase(title));
		
	}
	
	@PostMapping
	public ResponseEntity<PostModel> postBody(@RequestBody PostModel post){
		
		return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(post));
		
	}
	
	@PutMapping
	public ResponseEntity<PostModel> putBody(@RequestBody PostModel post){
		
		return ResponseEntity.status(HttpStatus.OK).body(repository.save(post));
		
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		
		repository.deleteById(id);
		
	}
	
	
}
