package com.github.PiotrDuma.documentationService.security;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.github.PiotrDuma.documentationService.exception.security.AuthenticationException;
import com.github.PiotrDuma.documentationService.model.AppUser;
import com.github.PiotrDuma.documentationService.service.dao.AppUserDao;


@ExtendWith(MockitoExtension.class)
public class UserNavigatorTest {

	@Mock
	private AppUserDao appUserDao;

	@InjectMocks
	private UserNavigatorImpl userNavigatorImpl;
	
	
	@Test
	void shouldReturnEmail() {

		Authentication auth = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(auth);
		SecurityContextHolder.setContext(securityContext);
		
		OidcUser user = mock(OidcUser.class);
		when(auth.getPrincipal()).thenReturn(user);
		when(user.getClaim("email")).thenReturn("test@email.com");
		
		assertEquals("test@email.com",userNavigatorImpl.getAppUserEmail());
	}
	
	@Test
	void shouldReturnEmailThrowException() {

//		Authentication auth = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);
		SecurityContextHolder.setContext(securityContext);
		
		assertThrows(AuthenticationException.class, () -> userNavigatorImpl.getAppUserEmail());
	}

	
	@Test
	void shouldReturnAppUserId() {
		UserNavigatorImpl spy = spy(userNavigatorImpl);
		doReturn("test@email.com").when(spy).getAppUserEmail();
		
		AppUser user = new AppUser("name", "test@email.com");
		user.setId(1);
		
		when(appUserDao.isEmailInDatabase(anyString())).thenReturn(true);
		when(appUserDao.get(anyString())).thenReturn(Optional.of(user));
		
		assertEquals(1, spy.getAppUserId());
	}
	
	@Test
	void getAppUserIdShouldThrowException() {
		UserNavigatorImpl spy = spy(userNavigatorImpl);
		doReturn("email").when(spy).getAppUserEmail();

		when(appUserDao.isEmailInDatabase(anyString())).thenReturn(false);
	
		assertThrows(AuthenticationException.class, () ->spy.getAppUserId());
	}
	
	@Test
	void shouldReturnAppUser() {
		AppUser user = new AppUser("name", "test@email.com");
		UserNavigatorImpl spy = spy(userNavigatorImpl);
		
		doReturn("email").when(spy).getAppUserEmail();
		when(appUserDao.get(anyString())).thenReturn(Optional.of(user));

		assertEquals(user, spy.getAppUser());
	}
	
	@Test
	void shouldReturnAppUserWhenReturnNull() {
		UserNavigatorImpl spy = spy(userNavigatorImpl);
		
		doReturn("email").when(spy).getAppUserEmail();
		when(appUserDao.isEmailInDatabase(anyString())).thenReturn(true);
		when(appUserDao.get(anyString())).thenReturn(Optional.empty());

		assertNull(spy.getAppUserId());
	}
	
	
	@Test
	void getAppUserShouldThrowException() {
		UserNavigatorImpl spy = spy(userNavigatorImpl);
		doReturn("email").when(spy).getAppUserEmail();

		when(appUserDao.get(anyString())).thenReturn(Optional.empty());

		assertThrows(AuthenticationException.class, () ->spy.getAppUser());
	}
}
