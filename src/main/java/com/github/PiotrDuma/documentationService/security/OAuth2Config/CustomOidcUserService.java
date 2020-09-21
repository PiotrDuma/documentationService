package com.github.PiotrDuma.documentationService.security.OAuth2Config;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.github.PiotrDuma.documentationService.model.AppUser;
import com.github.PiotrDuma.documentationService.service.dao.AppUserDao;

@Service
public class CustomOidcUserService extends OidcUserService{

	private final AppUserDao appUserDao;
	
	@Autowired
	public CustomOidcUserService(AppUserDao appUserDao) {
		this.appUserDao = appUserDao;
	}

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		OidcUser oidcUser = super.loadUser(userRequest);

		try {
			return storeUser(oidcUser);
		}catch(Exception ex) {
			throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
		}

	}

	private OidcUser storeUser(OidcUser oidcUser) {
		Map<String, Object> map = oidcUser.getAttributes();
		String email = (String)map.get("email");
		
		if(!appUserDao.isEmailInDatabase(email)){
			AppUser appUser = new AppUser();
			appUser.setEmail(email);
			appUser.setUsername((String)map.get("name"));
			appUserDao.save(appUser);
		}
		return oidcUser;
	}
}
