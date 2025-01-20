package com.footpath.store.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.jsonwebtoken.SignatureException;

@ControllerAdvice
public class ControllerExceptionAdvice extends ResponseEntityExceptionHandler{

	
	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<Object> invalidSignature(SignatureException ex) {
		System.out.println("exception handler called");
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("jwt invalid");
	}
}
