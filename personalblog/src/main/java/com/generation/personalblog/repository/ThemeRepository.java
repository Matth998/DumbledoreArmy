package com.generation.personalblog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.personalblog.model.ThemeModel;

public interface ThemeRepository extends JpaRepository<ThemeModel, Long>{

	public List<ThemeModel> findAllByDescriptionContainingIgnoreCase(String description);
	
}
