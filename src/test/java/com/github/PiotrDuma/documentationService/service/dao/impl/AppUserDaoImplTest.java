package com.github.PiotrDuma.documentationService.service.dao.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.PiotrDuma.documentationService.model.AppUser;
import com.github.PiotrDuma.documentationService.repository.AppUserRepo;


@ExtendWith(MockitoExtension.class)
public class AppUserDaoImplTest {

	@Mock
	AppUserRepo appUserRepo;

	@InjectMocks
	AppUserDaoImpl appUserDaoImpl;
	
	
	@Test
	void updateAppUser() {
		
		AppUser test = new AppUser("name", "email@email.com");
		test.setId(1);
		
		AppUser edited = new AppUser("changedname", "changedemail@email.com");
		edited.setId(1);
		
		when(appUserRepo.findById(1L)).thenReturn(Optional.of(test));
		when(appUserRepo.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

		assertEquals(edited, appUserDaoImpl.update(1L, edited));
	}
	
	@Test
	void throwUpdateWhenAppUserNotExists() {
		AppUser test = new AppUser("name", "email@email.com");
		test.setId(1);
		
		when(appUserRepo.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class,() -> appUserDaoImpl.update(1L, test));
	}
	
	@Test
	void throwDeleteWhenAppUserNotExists() {
		when(appUserRepo.existsById(anyLong())).thenReturn(false);
		assertThrows(EntityNotFoundException.class,() -> appUserDaoImpl.delete(1L));
	}

}
