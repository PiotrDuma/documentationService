package com.github.PiotrDuma.documentationService.security.OAuth2Config;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

/**
 * Configure custom authentication request. Extend request scopes and properties.
 *	Append new parameter to oauth request for access refresh token.
 *
 *		sources:
 *		https://stackoverflow.com/questions/58560201/spring-google-oauth2-with-refresh-token
 *		https://docs.spring.io/spring-security/site/docs/5.1.1.RELEASE/reference/htmlsingle/#oauth2Client-authorization-request-resolver
 *		https://developers.google.com/identity/protocols/oauth2/web-server#creatingclient
 */
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
	private final OAuth2AuthorizationRequestResolver defaultAuthorizationRequestResolver;

	public CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {

		this.defaultAuthorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
				clientRegistrationRepository, "/oauth2/authorization");
	}

	private OAuth2AuthorizationRequest customAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest) {
		Map<String, Object> additionalParameters = new LinkedHashMap<>(
				authorizationRequest.getAdditionalParameters());
		
		// set additional request parameter for refresh_token
		additionalParameters.put("access_type", "offline");
		additionalParameters.put("include_granted_scopes", "true");
		
		Set<String> scopes = new HashSet<String>(authorizationRequest.getScopes());
		scopes.add("https://www.googleapis.com/auth/gmail.send");
//		scopes.add("https://mail.google.com/");

		return OAuth2AuthorizationRequest.from(authorizationRequest)
				.additionalParameters(additionalParameters)
				.scopes(scopes)
				.build();
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
		OAuth2AuthorizationRequest authorizationRequest = this.defaultAuthorizationRequestResolver.resolve(request);

		return authorizationRequest != null ? customAuthorizationRequest(authorizationRequest) : null;
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {

		OAuth2AuthorizationRequest authorizationRequest = this.defaultAuthorizationRequestResolver.resolve(request,
				clientRegistrationId);

		return authorizationRequest != null ? customAuthorizationRequest(authorizationRequest) : null;
	}
}