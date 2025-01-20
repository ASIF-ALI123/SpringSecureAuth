package com.footpath.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.footpath.store.model.AddressEntity;
import com.footpath.store.model.UserEntity;


public interface AddressDAO extends JpaRepository<AddressEntity, Integer>{
	
	AddressEntity findByUser(UserEntity user);

}
