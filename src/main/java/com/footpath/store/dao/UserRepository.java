package com.footpath.store.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.footpath.store.model.UserEntity;


@Repository
public class UserRepository {

	@Autowired
	private UserDAO userDao;

	@Autowired
	private RoleDAO roleDAO;
	
	@Autowired
	private AddressDAO addressDAO;
	
	@Autowired
	private UserActivityDAO userActivityDAO;
	
	@Autowired
	private VerificationTokenDAO verificationTokenDAO;

	public UserEntity isUserExists(String userName) {
		return userDao.findByIdUserName(userName);
	}
	
	public UserEntity isUserExistByEmail(String mailId) {
		return userDao.findByEmailId(mailId);
	}

	public UserDAO getUserDAO() {
		return userDao;
	}

	public RoleDAO getRoleDAO() {
		return roleDAO;
	}

	public AddressDAO getAddressDAO() {
		return addressDAO;
	}

	public UserActivityDAO getUserActivityDAO() {
		return userActivityDAO;
	}

	public VerificationTokenDAO getVerificationTokenDAO() {
		return verificationTokenDAO;
	}
	
}
