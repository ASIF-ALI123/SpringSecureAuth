package com.footpath.store.business.bean;

import com.footpath.store.model.common.UserId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserBean {
	
	private UserId id;

	private String emailId;

	private String fName;

	private String lName;

	private String age;

}
