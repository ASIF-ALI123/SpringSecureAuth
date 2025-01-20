package com.footpath.store.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.footpath.store.model.UserEntity;
import com.footpath.store.service.UserServiceImpl;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserServiceImpl userServiceImpl;

	@Override
	public UserDetails loadUserByUsername(String usernameOrMailId) throws UsernameNotFoundException {
		
		List<SimpleGrantedAuthority> roles = new ArrayList<>();
		UserEntity registeredUser = null;
		System.out.println(usernameOrMailId);
		registeredUser = userServiceImpl.isUserExists(usernameOrMailId);// if userName provided
		if (registeredUser != null) {

			registeredUser.getRoles().forEach(role -> {
				roles.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
			});
			return new User(registeredUser.getId().getUserName(), registeredUser.getPassword(), roles);
		}
		registeredUser = userServiceImpl.isUserExistByEmail(usernameOrMailId);// if mailId provided
		if (registeredUser != null) {

			registeredUser.getRoles().forEach(role -> {
				roles.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
			});

			return new User(registeredUser.getId().getUserName(), registeredUser.getPassword(), roles);
		}
		

		System.err.println("---CustomUserDetailsService -- loadUserByUsername");
		throw new UsernameNotFoundException("Username or email doesnot exists");
	}

}
