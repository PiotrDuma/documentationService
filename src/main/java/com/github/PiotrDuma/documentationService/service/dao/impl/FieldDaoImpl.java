package com.github.PiotrDuma.documentationService.service.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.PiotrDuma.documentationService.model.AppUser;
import com.github.PiotrDuma.documentationService.model.Field;
import com.github.PiotrDuma.documentationService.repository.FieldRepo;
import com.github.PiotrDuma.documentationService.security.UserNavigator;
import com.github.PiotrDuma.documentationService.service.dao.AppUserDao;
import com.github.PiotrDuma.documentationService.service.dao.FieldDao;

@Service
public class FieldDaoImpl implements FieldDao {

	private final FieldRepo fieldRepo;
	private final AppUserDao appUserDao;
	private final UserNavigator nav;

	@Autowired
	public FieldDaoImpl(FieldRepo fieldRepo, AppUserDao appUserDao, UserNavigator nav) {
		this.fieldRepo = fieldRepo;
		this.appUserDao = appUserDao;
		this.nav = nav;
	}

	@Override
	@Transactional
	public Field save(Field field) {
		AppUser user;
		if(field.getAppUser()!=null) {
			user = field.getAppUser(); //app user cannot be null, set field user before save field
		}else {
			user = nav.getAppUser(); //or just set user to default logged in. 
		}
		user.addField(field);
		appUserDao.update(user.getId(),user);
//		return appUserRepo.save(user).getFields().stream().filter(e -> e.equals(field)).findAny().get();
		return field;
	}

	@Override
	@Transactional
	public Field update(Long id, Field field) {
		return fieldRepo.findById(id).map(e -> {
			e.setName(field.getName());
			e.setFieldNumber(field.getFieldNumber());
			e.setType(field.getType());
			e.setNote(field.getNote());
			e.setFieldDetails(field.getFieldDetails());
			return fieldRepo.save(e);
		}).orElseThrow(() ->  new EntityNotFoundException());
	}
	
	@Override
	@Transactional
	public Field saveOrUpdate(Field field) {
		Long id = field.getId();
		if(id != null) {// && fieldRepo.existsById(id)
			return update(id, field);
		}else {
			return save(field);
		}
	}

	@Override
	@Transactional
	public void delete(Long id) {
		if (fieldRepo.existsById(id)) {
			Field field = fieldRepo.findById(id).get();
			AppUser user = field.getAppUser();
			user.removeField(field);
			appUserDao.update(user.getId(),user); // cascade save
		} else {
			throw new EntityNotFoundException();
		}
	}

	@Override
	public Optional<Field> get(Long id) {
		return fieldRepo.findById(id);
	}

	@Override
	public List<Field> getAll() {
		List<Field> list = new ArrayList<Field>();
		fieldRepo.findAll().forEach(e -> list.add(e));
		return list;
	}

	@Override
	public List<Field> getAllByAppUser(AppUser appUser) {
		return fieldRepo.findAllByAppUser(appUser);
	}

	@Override
	public boolean isPreset(Field field) {
		return fieldRepo.existsById(field.getId());
	}

	@Override
	public boolean isPreset(Long id) {
		return fieldRepo.existsById(id);
	}
}
