package com.footpath.store.service;

import org.springframework.stereotype.Service;

import com.footpath.store.model.AddressEntity;
import com.footpath.store.model.UserEntity;
import com.footpath.store.model.common.ResponseBOModel;


public interface UserService {

	public ResponseBOModel registerUser(UserEntity reqUser);
	
	boolean sendActivationToken(UserEntity userEntity);
	
	UserEntity isUserExists(String userName);
	
	UserEntity isUserExistByEmail(String mailId);
	
	ResponseBOModel confirmAndActivateUserId(String token);
}
