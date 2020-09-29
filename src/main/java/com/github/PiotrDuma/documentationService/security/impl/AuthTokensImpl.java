package com.github.PiotrDuma.documentationService.security.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.PiotrDuma.documentationService.exception.security.AuthenticationException;
import com.github.PiotrDuma.documentationService.security.AuthTokens;


@Controller
public class AuthTokensImpl implements AuthTokens {
	private static String TOKEN_API = "https://oauth2.googleapis.com/token";
	private OAuth2AuthorizedClientService authorizedClientService;

	@Autowired
	public AuthTokensImpl(OAuth2AuthorizedClientService authorizedClientService) {
		this.authorizedClientService = authorizedClientService;
	}

	@Override
	public String getAccessToken() {
		OAuth2AuthorizedClient authorizedClient = getOAuth2AuthorizedClient();
		OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
		return accessToken.getTokenValue();
	}

	@Override
	public Instant accessTokenExpiresAt() {
		OAuth2AuthorizedClient authorizedClient = getOAuth2AuthorizedClient();
		OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
		return accessToken.getExpiresAt();
	}

	// https://developers.google.com/identity/protocols/oauth2/web-server#httprest_7
	@Override
	public String refreshAccessToken() {
		String response = "test";

		try {
			URL url = new URL(TOKEN_API);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Accept", "application/json");

			DataOutputStream stream = new DataOutputStream(conn.getOutputStream());
			byte[] req = prepareRequest().getBytes("UTF-8");
			stream.write(req);

			try (BufferedReader in = new BufferedReader(
						new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
				
				StringBuilder temp = new StringBuilder();
				String buff = null;
				while ((buff = in.readLine()) != null) {
					System.out.println(buff);
					temp.append(buff.trim());
				}
				response = temp.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resolveResponse(response);
	}
	
	@Override
	public String getEmailAdress() {
		try { // authentication manager provides to create authentication.
			OidcUser user = (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return user.getEmail();
		} catch (Exception ex) {
			String message = "Authentication cast to OAuth2AuthenticationToken failed.";
			System.out.println(message);
			ex.printStackTrace();
			throw new AuthenticationException(message);
		}
	}

	// https://spring.io/blog/2018/03/06/using-spring-security-5-to-integrate-with-oauth-2-secured-services-such-as-facebook-and-github
	private OAuth2AuthorizedClient getOAuth2AuthorizedClient() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		OAuth2AuthenticationToken oauthToken = null;

		try { // authentication manager provides to create authentication.
			oauthToken = (OAuth2AuthenticationToken) auth;
		} catch (Exception ex) {
			String message = "Authentication cast to OAuth2AuthenticationToken failed.";
			System.out.println(message);
			ex.printStackTrace();
			throw new AuthenticationException(message);
		}
		return this.authorizedClientService.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(),
				oauthToken.getName());
	}

	private String prepareRequest() {
		OAuth2AuthorizedClient authorizedClient = getOAuth2AuthorizedClient();
		String oauthClientId = authorizedClient.getClientRegistration().getClientId();
		String oauthSecret = authorizedClient.getClientRegistration().getClientSecret();
		String refreshToken = authorizedClient.getRefreshToken().getTokenValue();

		try {

			StringBuffer request = new StringBuffer()
					.append("client_id=").append(URLEncoder.encode(oauthClientId, "UTF-8"))
					.append("&client_secret=").append(URLEncoder.encode(oauthSecret, "UTF-8"))
					.append("&refresh_token=").append(URLEncoder.encode(refreshToken, "UTF-8"))
					.append("&grant_type=refresh_token");
			return request.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String resolveResponse(String response) {
		try {
			HashMap<String, Object> result = new ObjectMapper()
					.readValue(response, new TypeReference<HashMap<String, Object>>() {
					});
			return (String)result.get("access_token");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

//	public Map<String, Object> getUserInfo() {
//		OAuth2AuthorizedClient authorizedClient = getOAuth2AuthorizedClient();
//
//		OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
//		OAuth2RefreshToken refreshToken = authorizedClient.getRefreshToken();
//
//		String oauthClientId = authorizedClient.getClientRegistration().getClientId();
//		String oauthSecret = authorizedClient.getClientRegistration().getClientSecret();
//		String accessTokenVal = accessToken.getTokenValue();
//		String refreshTokenVal = refreshToken.getTokenValue();
//		Instant tokenExpires = accessToken.getExpiresAt();
//		
//		Map<String, Object> info = new HashMap<String, Object>();
//		info.put("oauthClientId", oauthClientId);
//		info.put("oauthSecret", oauthSecret);
//		info.put("accessToken", accessTokenVal);
//		info.put("refreshToken", refreshTokenVal);
//		info.put("tokenExpires", tokenExpires);
//		return info;
//	}
}
