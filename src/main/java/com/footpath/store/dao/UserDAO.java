package com.footpath.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.footpath.store.model.UserEntity;
import com.footpath.store.model.VerificationTokenEntity;


public interface UserDAO extends JpaRepository<UserEntity, Integer> {
	
	UserEntity findByIdUserName(String userId);
	
	UserEntity findByEmailId(String emailId);
	
	UserEntity findByVerificationToken(VerificationTokenEntity tokenEntity);
	
	void deleteByVerificationToken(VerificationTokenEntity verificationTokenEntity);
	

}
