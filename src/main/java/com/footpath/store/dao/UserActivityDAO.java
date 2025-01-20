package com.footpath.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.footpath.store.model.UserActivityEntity;


public interface UserActivityDAO extends JpaRepository<UserActivityEntity, Long> {

}
