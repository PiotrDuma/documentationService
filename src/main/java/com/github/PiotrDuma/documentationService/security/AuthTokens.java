package com.github.PiotrDuma.documentationService.security;

import java.time.Instant;

public interface AuthTokens {
	String getAccessToken();
	Instant accessTokenExpiresAt();
	String getEmailAdress();
	String refreshAccessToken();
}
