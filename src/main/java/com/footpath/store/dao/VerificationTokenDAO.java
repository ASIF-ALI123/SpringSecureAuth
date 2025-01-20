package com.footpath.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.footpath.store.model.VerificationTokenEntity;

public interface VerificationTokenDAO extends JpaRepository<VerificationTokenEntity, Integer> {
	
	VerificationTokenEntity findByToken(String token);

}
