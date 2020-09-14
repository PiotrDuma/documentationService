package com.github.PiotrDuma.documentationService.service.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.PiotrDuma.documentationService.model.AppUser;
import com.github.PiotrDuma.documentationService.repository.AppUserRepo;
import com.github.PiotrDuma.documentationService.service.dao.AppUserDao;

@Service(value = "appUserDao")
public class AppUserDaoImpl implements AppUserDao {

	AppUserRepo appUserRepo;

	@Autowired
	public AppUserDaoImpl(AppUserRepo appUserRepo) {
		this.appUserRepo = appUserRepo;
	}

	@Override
	@Transactional
	public AppUser save(AppUser appUser) {
		return appUserRepo.save(appUser);
	}
	
	@Override
	@Transactional
	public AppUser update(Long id, AppUser appUser) {
		return appUserRepo.findById(id).map(e -> {
			e.setUsername(appUser.getUsername());
			e.setEmail(appUser.getEmail());
//			e.setFields(appUser.getFields()); //inefficient with many elements.
			return appUserRepo.save(e);
		}).orElseThrow(EntityNotFoundException::new);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		if(appUserRepo.existsById(id)){
			appUserRepo.deleteById(id);
		}else {
			throw new EntityNotFoundException();
		}
	}


	@Override
	public Optional<AppUser> get(Long id) {
		return appUserRepo.findById(id);
	}
	
	@Override
	public Optional<AppUser> get(String email) {
		return appUserRepo.findAppUserByEmail(email);
	}

	@Override
	public List<AppUser> getAll() {
		List<AppUser> list = new ArrayList<AppUser>();
		appUserRepo.findAll().forEach(e -> list.add(e));
		return list;
	}

	@Override
	public boolean isPreset(AppUser appUser) {
		return appUserRepo.existsById(appUser.getId());
	}
	
	@Override
	public boolean isPreset(Long id) {
		return appUserRepo.existsById(id);
	}

	@Override
	public boolean isEmailInDatabase(String email) {
		return appUserRepo.findAppUserByEmail(email).isPresent();
	}
}
