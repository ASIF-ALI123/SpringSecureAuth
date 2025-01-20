package com.footpath.store.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.footpath.store.model.common.ResponseBOModel;
import com.footpath.store.security.CustomUserDetailsService;
import com.footpath.store.security.JwtUtil;
import com.footpath.store.security.model.AuthenticationRequest;
import com.footpath.store.security.model.AuthenticationResponse;

import io.jsonwebtoken.impl.DefaultClaims;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class UserLoginController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private JwtUtil jwtUtilService;

	@Autowired
	RestTemplate restTemplate;
	

	@PostMapping("/authenticate")
	public String createAuthenticationToken(HttpServletRequest req)// @RequestBody AuthenticationRequest
																	// authenticationRequest,
			{

		System.out.println("-----in authenticate-----");
		String token = null;
		Base64.Decoder decoder = Base64.getDecoder();
		// Decoding string
		String dStr = new String(decoder
				.decode(req.getHeader("Authorization").substring(6, req.getHeader("Authorization").length())));

		String[] authReq = dStr.split(":");
		try {

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authReq[0], authReq[1]));


		} catch (DisabledException e) {
			System.out.println("disables");
			//throw new Exception("USER_ID DISABLED", e);
		} catch (BadCredentialsException e) {
			System.out.println("invalid");
			//throw new Exception("INVALID_CREDENTIALS", e);
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
			System.err.println("---Login parameter INVALID---");
		}

		UserDetails userDetails = customUserDetailsService.loadUserByUsername(authReq[0]);

		token = jwtUtilService.generateJwtToken(userDetails);
		System.out.println(token);
		return token;
	}

	@RequestMapping(value = "/refreshtoken", method = RequestMethod.GET)
	public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
		// From the HttpRequest get the claims
		DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

		Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
		String token = jwtUtilService.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);

		return new ResponseEntity(new AuthenticationResponse(token), headers, HttpStatus.OK);
	}

	public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
		Map<String, Object> expectedMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : claims.entrySet()) {
			expectedMap.put(entry.getKey(), entry.getValue());
		}
		return expectedMap;
	}

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

	private String getBody(final User user) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(user);
	}
}
