package com.footpath.store.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.footpath.store.model.UserEntity;

@RestController
@RequestMapping("/api/v1")
public class ResourceController {
	public static int i = 100;
	
	
	@GetMapping(value = "/allProduct", produces="application/json")
	public List<UserEntity> getAllProducts() {
		String res = "all products" + ++i;
		
		List<UserEntity> l = new ArrayList<>();
		UserEntity u1 = new UserEntity();
		u1.setfName(""+i);
		l.add(u1);
		l.add(new UserEntity());
		l.add(new UserEntity());
		
		return l;
	}

}
