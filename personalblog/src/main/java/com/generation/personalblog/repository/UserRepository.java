package com.generation.personalblog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.generation.personalblog.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

	public Optional<UserModel> findByUser(String user);

	public List<UserModel> findAllByNameContainingIgnoreCase(String name);

}
