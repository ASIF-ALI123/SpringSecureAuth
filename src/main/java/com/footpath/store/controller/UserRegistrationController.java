package com.footpath.store.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.footpath.store.exceptions.CustomUserAuthenticationException;
import com.footpath.store.model.UserEntity;
import com.footpath.store.model.common.ResponseBOModel;
import com.footpath.store.service.UserServiceImpl;

@RestController
@RequestMapping("/auth")
public class UserRegistrationController {

	@Autowired
	private UserServiceImpl userServiceImpl;

	@PostMapping(path = "/new")
	public ResponseEntity registerUser(@RequestBody UserEntity user) throws CustomUserAuthenticationException {
		ResponseBOModel responseModel = null;
		try {
			responseModel = userServiceImpl.registerUser(user);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER FAILURE! USER REGISTRATION NOT SUCCESS!");
		}

		return new ResponseEntity(responseModel, HttpStatus.CREATED);
	}
	
	@PostMapping(path = "/batch/new")
	public ResponseEntity registerUser(@RequestBody ArrayList<UserEntity> userList) throws CustomUserAuthenticationException {
		
		try {
			userList.forEach(user -> {
				userServiceImpl.registerUser(user);				
			});
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SERVER FAILURE! USER REGISTRATION NOT SUCCESS!");
		}

		return ResponseEntity.status(HttpStatus.CREATED).body("All user registered");
	}

	@PostMapping(path = "/verify")
	public boolean reActiveAccount(@RequestParam("id") String userNameORmailId) {
		System.out.println(userNameORmailId);

		UserEntity registeredUserByUserName = userServiceImpl.isUserExists(userNameORmailId);

		if (registeredUserByUserName != null && !registeredUserByUserName.getUserActivity().isActive()) {
			System.out.println("ok1");
			userServiceImpl.sendActivationToken(registeredUserByUserName);
			return true;
		}
		
		UserEntity registeredUserByEmail = userServiceImpl.isUserExistByEmail(userNameORmailId);

		if (registeredUserByEmail != null && !registeredUserByEmail.getUserActivity().isActive()) {
			System.out.println("ok2");
			userServiceImpl.sendActivationToken(registeredUserByEmail);
			return true;
		}

		return false;
	}
	
	@GetMapping("/confirmAccount")
	public ResponseBOModel confirmAccountByToken(@RequestParam("token") String token) {
		
		ResponseBOModel responseModel = userServiceImpl.confirmAndActivateUserId(token);
		
		if(responseModel != null)
			return responseModel;
		
		return null;
	}

}
