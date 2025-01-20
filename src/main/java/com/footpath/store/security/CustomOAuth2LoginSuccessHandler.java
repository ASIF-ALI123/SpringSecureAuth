package com.footpath.store.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.footpath.store.service.UserServiceImpl;

@Component
public class CustomOAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
		Map attributes = oAuth2User.getAttributes();

		UserDetails savedUser = customUserDetailsService.loadUserByUsername((String) attributes.get("email"));

		String token = jwtUtil.generateJwtToken(savedUser);
		response.getWriter().write(
				mapper.writeValueAsString(Collections.singletonMap("accessToken", token)));


	}
}
