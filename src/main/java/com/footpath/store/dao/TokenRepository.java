package com.footpath.store.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.footpath.store.model.JwtTokenEntity;

@Repository
public class TokenRepository {

	@Autowired
	private JwtTokenDAO jwtTokenDAO;
	
	public boolean saveJwtToken(String userNameOrEmailId, String token) {
		if(userNameOrEmailId != null && token != null) {
		JwtTokenEntity t = new JwtTokenEntity();
		t.setUserName(userNameOrEmailId);
		t.setToken(token);
			return jwtTokenDAO.save(t) != null;
		}
		return false;
	}
	
}
