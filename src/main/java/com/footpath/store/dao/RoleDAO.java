package com.footpath.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.footpath.store.model.RoleEntity;
import com.footpath.store.model.enums.RoleType;


public interface RoleDAO extends JpaRepository<RoleEntity, Integer>{
	

	RoleEntity findByName(RoleType name);

}
