package com.github.PiotrDuma.documentationService.security;

import com.github.PiotrDuma.documentationService.model.AppUser;

public interface UserNavigator {
	public String getAppUserEmail();
	public Long getAppUserId();
	public AppUser getAppUser();
}
