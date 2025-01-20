package com.footpath.store.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.footpath.store.model.common.UserId;

import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "user")
public class UserEntity implements Serializable {

	@EmbeddedId
	private UserId id;

	@Column(nullable = false, unique = true, updatable = false)
	private String emailId;

	@Column(name = "firstName")
	private String fName;

	@Column(name = "lastName")
	private String lName;

	private String age;
	
	private String password;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_role", joinColumns = {
			@JoinColumn(name = "userName_FK", referencedColumnName = "userName") }, inverseJoinColumns = {
					@JoinColumn(name = "roleId_FK", referencedColumnName = "Id") })
	private Set<RoleEntity> roles = new HashSet<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@MapKey(name = "id")
	private List<AddressEntity> addressEntity = new ArrayList<>();
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private UserActivityEntity userActivity; 
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private VerificationTokenEntity verificationToken;

	public UserId getId() {
		return id;
	}

	public void setId(UserId id) {
		this.id = id;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}

	@JsonManagedReference
	public List<AddressEntity> getAddressEntity() {
		return addressEntity;
	}

	public void setAddressEntity(List<AddressEntity> addressEntity) {
		this.addressEntity = addressEntity;
	}

	@JsonManagedReference
	public UserActivityEntity getUserActivity() {
		return userActivity;
	}

	public void setUserActivity(UserActivityEntity userActivity) {
		this.userActivity = userActivity;
	}

	@JsonManagedReference
	public VerificationTokenEntity getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(VerificationTokenEntity verificationToken) {
		this.verificationToken = verificationToken;
	}
	
}
