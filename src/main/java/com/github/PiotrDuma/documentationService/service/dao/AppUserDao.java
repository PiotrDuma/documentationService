package com.github.PiotrDuma.documentationService.service.dao;

import java.util.Optional;

import com.github.PiotrDuma.documentationService.model.AppUser;

public interface AppUserDao extends DaoInterface<AppUser>{

	Optional<AppUser> get(String email);
	boolean isEmailInDatabase(String email);
}
