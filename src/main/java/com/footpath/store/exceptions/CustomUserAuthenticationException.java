package com.footpath.store.exceptions;

import com.footpath.store.model.common.AuthenticationResponse;

public class CustomUserAuthenticationException extends Exception {
	
	private AuthenticationResponse authenticationResponse;

	public CustomUserAuthenticationException(String exceptionMessage) {
		super(exceptionMessage);
	}

}
