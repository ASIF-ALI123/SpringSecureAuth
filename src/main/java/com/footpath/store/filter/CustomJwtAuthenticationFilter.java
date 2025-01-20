package com.footpath.store.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.footpath.store.security.CustomOAuth2Userservice;
import com.footpath.store.security.CustomUserDetailsService;
import com.footpath.store.security.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Component
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private CustomUserDetailsService userDetailsService;
	@Autowired
	private CustomOAuth2Userservice s;

	Collection<String> excludeUrlPatterns = new ArrayList<>();
	

    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs",
            "/v3/api-docs",  
            "/swagger-resources/**", 
            "/swagger-ui/**",
            "/swagger-ui.html/**",
             };

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		System.out.println("filter");

		String authorizationHeader = request.getHeader("Authorization");
		String userName = null;
		String token = null;

		try {
			
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
				token = authorizationHeader.substring(7, authorizationHeader.length());
				System.out.println(token);
				System.out.println(jwtUtil.getUserNameFromToken(token));
				userName = jwtUtil.getUserNameFromToken(token);

				if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					System.out.println("validation token");

					if (jwtUtil.validateToken(token)) {
						System.out.println("setting in context");
						UsernamePasswordAuthenticationToken userNamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								jwtUtil.getUserNameFromToken(token), null, jwtUtil.getRolesFormToken(token));
						userNamePasswordAuthenticationToken
								.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

						SecurityContextHolder.getContext().setAuthentication(userNamePasswordAuthenticationToken);
					}
				}
			}else {
				SecurityContextHolder.getContext().setAuthentication(null);
			}
			filterChain.doFilter(request, response);


		} catch (ExpiredJwtException ex) {
			System.out.println("exception expire" + ex.getClaims() + ex.getHeader());

			String isRefreshToken = request.getHeader("isRefreshToken");
			String requestURL = request.getRequestURL().toString();
			// allow for Refresh Token creation if following conditions are true.
			if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refreshtoken")) {
				allowForRefreshToken(ex, request);
			} else
				request.setAttribute("exception", ex);
			
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.getWriter().write(convertObjectToJson("TOKEN EXPIRED"));
		} catch (BadCredentialsException ex) {
			System.out.println("BAD JWT");
			request.setAttribute("exception", "BAD CREDENTIAL");

			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.getWriter().write(convertObjectToJson("BAD REQUEST"));
		} catch (SignatureException ex) {
			System.out.println(ex + "TOKEN INVALID");

			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.getWriter().write(convertObjectToJson("TOKEN INVALID"));

		} catch (Exception e) {
			System.out.println("FATAL EXCEPTION" + e);

			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.getWriter().write(convertObjectToJson("FATAL EXCEPTION"));
		}

	}

	/*
	 * @Override protected boolean shouldNotFilter(HttpServletRequest request)
	 * throws ServletException { AntPathMatcher pathMatcher = new AntPathMatcher();
	 * System.out.println("exculde");
	 * //excludeUrlPatterns.add("/oauth2User/success");
	 * //excludeUrlPatterns.add("/secure/register/**");
	 * 
	 * System.out.println(request.getServletPath() + "<--exc");
	 * 
	 * return excludeUrlPatterns.stream().anyMatch(p -> pathMatcher.match(p,
	 * request.getServletPath())); }
	 */

	public String convertObjectToJson(Object object) throws JsonProcessingException {
		if (object == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}

	private String extractTokenFromRequets(HttpServletRequest request) {

		String bearerToken = request.getHeader("Authorization");// value = Authorization

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) { // if value = Authorization if
																						// block execute
			System.out.println(bearerToken);
			return bearerToken.substring(7, bearerToken.length());
		}
		return bearerToken;
	}

	private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {

		// create a UsernamePasswordAuthenticationToken with null values.
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				null, null, null);
		// After setting the Authentication in the context, we specify
		// that the current user is authenticated. So it passes the
		// Spring Security Configurations successfully.
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		// Set the claims so that in controller we will be using it to create
		// new JWT
		request.setAttribute("claims", ex.getClaims());

	}
}
