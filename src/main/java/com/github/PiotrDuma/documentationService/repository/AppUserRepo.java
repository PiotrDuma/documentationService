package com.github.PiotrDuma.documentationService.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.github.PiotrDuma.documentationService.model.AppUser;

@Repository(value = "AppUserRepo")
public interface AppUserRepo extends CrudRepository<AppUser, Long> {

	public Optional<AppUser> findAppUserByUsername(String username);
	public Optional<AppUser> findAppUserByEmail(String email);
}
