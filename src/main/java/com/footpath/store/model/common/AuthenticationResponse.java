package com.footpath.store.model.common;

import com.footpath.store.model.enums.ResponseType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
	
	private Boolean status;
	private ResponseType responseType;	

}
