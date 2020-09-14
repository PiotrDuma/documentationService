package com.github.PiotrDuma.documentationService.service.dao;

import java.util.List;

import com.github.PiotrDuma.documentationService.model.AppUser;
import com.github.PiotrDuma.documentationService.model.Field;

public interface FieldDao extends DaoInterface<Field>{
	List<Field> getAllByAppUser(AppUser appUser);
	Field saveOrUpdate(Field field);
}
