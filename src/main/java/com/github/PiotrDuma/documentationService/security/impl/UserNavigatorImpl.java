package com.github.PiotrDuma.documentationService.security.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.github.PiotrDuma.documentationService.exception.security.AuthenticationException;
import com.github.PiotrDuma.documentationService.model.AppUser;
import com.github.PiotrDuma.documentationService.security.UserNavigator;
import com.github.PiotrDuma.documentationService.service.dao.AppUserDao;

@Service
public class UserNavigatorImpl implements UserNavigator{

	private AppUserDao appUserDao;

	@Autowired
	public UserNavigatorImpl(AppUserDao appUserDao) {
		this.appUserDao = appUserDao;
	}

	@Override
	public String getAppUserEmail(){
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			OidcUser oidcUser = (OidcUser)auth.getPrincipal();
			String email = oidcUser.getClaim("email");
			return email;
		}catch(Exception ex) {
			throw new AuthenticationException("Cannot recieve authentication object");
		}
	}
	
	@Override
	public Long getAppUserId(){
		String email = getAppUserEmail();
		if(!appUserDao.isEmailInDatabase(email)) {
			throw new AuthenticationException("Cannot recieve authentication object");
		}else {
			return appUserDao.get(email).map(AppUser::getId).orElse(null);
		}
	}
	
	@Override
	public AppUser getAppUser(){
		return appUserDao.get(getAppUserEmail()).orElseThrow(AuthenticationException::new);
	}
}
