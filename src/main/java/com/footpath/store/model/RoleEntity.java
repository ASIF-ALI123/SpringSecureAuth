package com.footpath.store.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.footpath.store.model.enums.RoleType;

import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class RoleEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private RoleType name;

	
	public RoleEntity(RoleType roleName) {
		super();
		this.name = roleName;
	}

	public RoleEntity() {
		super();
	}

}
