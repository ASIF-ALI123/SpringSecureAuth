package com.footpath.store.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.footpath.store.dao.TokenRepository;
import com.footpath.store.model.enums.RoleType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class JwtUtil {


	private String secret;
	
	private Integer jwtExpiritaionInMs;

	private Integer jwtRefreshExpiritaionInMs;
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Value("${jwt.secret}")
	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	@Value("${jwt.jwtExpirationInMs}")
	public void setJwtExpiritaionInMs(Integer jwtExpiritaionInMs) {
		this.jwtExpiritaionInMs = jwtExpiritaionInMs;
	}
	
	@Value("${jwt.jwtRefreshExpirationInMs}")
	public void setJwtRefreshExpiritaionInMs(Integer jwtRefreshExpiritaionInMs) {
		this.jwtRefreshExpiritaionInMs = jwtRefreshExpiritaionInMs;
	}
	
	public String generateJwtToken(UserDetails userDetails) {
		
		Map<String, Object> claims = new HashMap<>();
		Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
		
		if(roles.contains(new SimpleGrantedAuthority("ROLE_" + RoleType.USER))) {
			System.out.println("oauth user claim");
			claims.put("isUser", true);
		}
		if(roles.contains(new SimpleGrantedAuthority("ROLE_" + RoleType.ADMIN))) {
			claims.put("isAdmin", true);
		}
		if(roles.contains(new SimpleGrantedAuthority("ROLE_" + RoleType.EXTERNAL))) {
			claims.put("isExternal", true);
		}
		if(roles.contains(new SimpleGrantedAuthority("ROLE_" + RoleType.MOD))) {
			claims.put("isMod", true);
		}
		if(roles.contains(new SimpleGrantedAuthority("ROLE_" + RoleType.SELLER))) {
			claims.put("isSeller", true);
		}
		String token = getToken(claims, userDetails.getUsername());
		saveJwt(userDetails.getUsername(), token);
		return token;
	}
	
	private String getToken(Map<String, Object> claims, String subject) {
		
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+jwtExpiritaionInMs))
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}
	
	public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpiritaionInMs))
				.signWith(SignatureAlgorithm.HS512, secret).compact();

	}
	
	public boolean validateToken(String authToken) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
			return true;
		}catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
			throw new BadCredentialsException("INVALID_CREDENTIALS", e);
		}catch (ExpiredJwtException ex) {
			throw ex;
		}
	}
	
	public String getUserNameFromToken(String token) {
		System.out.println("-----");
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		
		return claims.getSubject();
	}
	
	public List<SimpleGrantedAuthority> getRolesFormToken(String token){
		
		List<SimpleGrantedAuthority> roles = new ArrayList<>();
		
		Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		
		Boolean isUser = claims.get("isUser", Boolean.class);
		Boolean isAdmin = claims.get("isAdmin", Boolean.class);
		Boolean isSeller = claims.get("isSeller", Boolean.class);
		Boolean isMod = claims.get("isMod", Boolean.class);
		Boolean isExternal = claims.get("isExternal", Boolean.class);
		
		if(isUser!=null && isUser) {
			roles.add(new SimpleGrantedAuthority("ROLE_" + RoleType.USER));
		}
		if(isAdmin!=null && isAdmin) {
			roles.add(new SimpleGrantedAuthority("ROLE_" + RoleType.ADMIN));
		}
		if(isSeller!=null && isSeller) {
			roles.add(new SimpleGrantedAuthority("ROLE_" + RoleType.SELLER));
		}
		if(isMod!=null && isMod) {
			roles.add(new SimpleGrantedAuthority("ROLE_" + RoleType.MOD));
		}
		if(isExternal!=null && isExternal) {
			roles.add(new SimpleGrantedAuthority("ROLE_" + RoleType.EXTERNAL));
		}
		
		return roles;
	}
	
	private boolean saveJwt(String userNameOrMailId, String token) {
		
		return tokenRepository.saveJwtToken(userNameOrMailId, token);
	}
	
}
