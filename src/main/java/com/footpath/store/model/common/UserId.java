package com.footpath.store.model.common;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class UserId implements Serializable {

	private String userName;

	public UserId() {
		super();
	}

	public UserId(String userName) {
		super();
		this.userName = userName;
	}

}
