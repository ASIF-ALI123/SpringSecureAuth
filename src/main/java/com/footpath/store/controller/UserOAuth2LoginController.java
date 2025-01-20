package com.footpath.store.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.footpath.store.model.UserEntity;
import com.footpath.store.security.CustomOAuth2User;
import com.footpath.store.security.CustomUserDetailsService;
import com.footpath.store.security.JwtUtil;
import com.footpath.store.security.model.AuthenticationResponse;
import com.footpath.store.service.UserServiceImpl;

@RestController
@RequestMapping("/oauth2")
public class UserOAuth2LoginController {

	private static String authorizationRequestBaseUri = "oauth2/authorization";
	Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

	@Autowired
	private ClientRegistrationRepository clientRegistrationRepository;

	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;

	@Autowired
	private JwtUtil jwtUtil;	

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@GetMapping("/token")
	public ResponseEntity<?> getLoginInfo() {
		
		System.out.println("Oauth scess controller!!!");
		System.out.println(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
		CustomOAuth2User oAuth2User = (CustomOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println("-----oauth2 002");
		Map attributes = oAuth2User.getAttributes();

		UserEntity entity = userServiceImpl.isUserExistByEmail((String) attributes.get("email"));
		
		UserDetails savedUser = customUserDetailsService.loadUserByUsername((String) attributes.get("email"));
		
		String token = jwtUtil.generateJwtToken(savedUser);

		MultiValueMap<String, String> headers = new HttpHeaders();

		headers.add("Authorization", token);
		
		return new ResponseEntity<>(new AuthenticationResponse(token), HttpStatus.OK);
		
	}
	
	@GetMapping("/loginFailure")
	public String oAuthLogInFail(Model model, OAuth2AuthenticationToken authentication) {

	    return "Fail to login!!!";
	}
	
	@GetMapping("/r1")
	public String getR1() {
		return "resource 1";
	}


	@GetMapping("/r2")
	public String getR2() {
		return "resource 2";
	}
   
	
	
	
	
}
