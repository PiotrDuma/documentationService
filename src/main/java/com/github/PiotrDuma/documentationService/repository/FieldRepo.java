package com.github.PiotrDuma.documentationService.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.github.PiotrDuma.documentationService.model.AppUser;
import com.github.PiotrDuma.documentationService.model.Field;

public interface FieldRepo extends CrudRepository<Field, Long>{

	List<Field> findAllByAppUser(AppUser appUser);
}
