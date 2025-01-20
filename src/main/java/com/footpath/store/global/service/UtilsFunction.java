package com.footpath.store.global.service;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.footpath.store.business.bean.AddressBean;
import com.footpath.store.business.bean.RoleBean;
import com.footpath.store.business.bean.UserBean;
import com.footpath.store.model.AddressEntity;
import com.footpath.store.model.RoleEntity;
import com.footpath.store.model.UserEntity;

@Service
public class UtilsFunction {

	public String generateRandomToken() {
		return UUID.randomUUID().toString();
	}

	public static UserBean getUserEntityToBean(UserEntity userEntity) {

		UserBean userBean = new UserBean();

		BeanUtils.copyProperties(userEntity, userBean);

		return userBean;
	}

	public static AddressBean getAddressEntityToBean(AddressEntity addressEntity) {

		AddressBean addressBean = new AddressBean();

		BeanUtils.copyProperties(addressEntity, addressBean);

		return addressBean;
	}

	public static RoleBean getRoleEntityToBean(RoleEntity roleEntity) {

		RoleBean roleBean = new RoleBean();

		BeanUtils.copyProperties(roleEntity, roleBean);

		return roleBean;
	}
	
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
