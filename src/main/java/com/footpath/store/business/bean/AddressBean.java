package com.footpath.store.business.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddressBean {
	private String addressLine;

	private String contactNo;

	private String district;

	private String state;

	private String country;

	private String pin;
}
