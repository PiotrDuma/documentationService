package com.github.PiotrDuma.documentationService.service.dao.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.PiotrDuma.documentationService.model.AppUser;
import com.github.PiotrDuma.documentationService.model.Field;
import com.github.PiotrDuma.documentationService.model.FieldDetails;
import com.github.PiotrDuma.documentationService.repository.FieldRepo;
import com.github.PiotrDuma.documentationService.security.UserNavigator;
import com.github.PiotrDuma.documentationService.service.dao.AppUserDao;

@ExtendWith(MockitoExtension.class)
public class FieldDaoImplTest {

	@Mock
	FieldRepo fieldRepo;
	@Mock
	AppUserDao appUserDao;
	@Mock
	UserNavigator nav;
	
	@InjectMocks
	FieldDaoImpl fieldDaoImpl;
	
	
	@Test
	void saveIfFieldUserIsNull() {
		Field element = new Field("name", 1, Field.FieldClass.KLASA1, "note", null);
		
		AppUser user = new AppUser("name", "email@test.com");
		user.setId(1L);
		
		when(nav.getAppUser()).thenReturn(user);
		when(appUserDao.update(1L, user)).thenReturn(user);
		
		//user has to have field in list of elements.
		assertEquals(element, fieldDaoImpl.save(element));
		assertEquals(user.getFields().size(), 1);
		assertTrue(user.getFields().contains(element));

		//field must have user set. 
		assertEquals(user, element.getAppUser());
	}
	
	@Test
	void updateField() {
		AppUser user = new AppUser();
		Field expected = new Field("name", 1, Field.FieldClass.KLASA1, "note", user);
		expected.setFieldDetails(new FieldDetails());
		expected.setId(1L);
		
		Field updated = new Field();
		updated.setAppUser(user);
		updated.setId(1L);
		
		when(fieldRepo.findById(anyLong())).thenReturn(Optional.of(updated));
		when(fieldRepo.save(any())).thenAnswer(item -> item.getArgument(0));
		
		assertEquals(expected, fieldDaoImpl.update(1L, expected));
	}
	
	@Test
	void updateFieldThrowWhenNotFound() {	
		when(fieldRepo.findById(anyLong())).thenReturn(Optional.empty());
		
		assertThrows(EntityNotFoundException.class,()-> fieldDaoImpl.update(1L, new Field()));
	}
	
	@Test
	void deleteField() {
		AppUser user = new AppUser();
		Field field = new Field("name", 1, Field.FieldClass.KLASA1, "note", user);
		field.setFieldDetails(new FieldDetails());
		field.setId(1L);
		
		user.addField(field);
		
		when(fieldRepo.existsById(anyLong())).thenReturn(true);
		when(fieldRepo.findById(anyLong())).thenReturn(Optional.of(field));
		when(appUserDao.update(anyLong(),eq(user))).thenAnswer(item -> item.getArgument(1));
		
		fieldDaoImpl.delete(1L);
		assertEquals(0, user.getFields().size());
	}
	
	@Test
	void deleteFieldThrowWhenNotFound() {	
		when(fieldRepo.existsById(anyLong())).thenReturn(false);
		
		assertThrows(EntityNotFoundException.class,()-> fieldDaoImpl.delete(1L));
	}
}
