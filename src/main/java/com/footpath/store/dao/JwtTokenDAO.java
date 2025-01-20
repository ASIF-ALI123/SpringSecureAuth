package com.footpath.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.footpath.store.model.JwtTokenEntity;

public interface JwtTokenDAO extends JpaRepository<JwtTokenEntity, String> {

}
