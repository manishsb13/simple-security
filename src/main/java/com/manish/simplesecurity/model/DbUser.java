package com.manish.simplesecurity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USERS", schema = "security" )
@Getter
@Setter
@NoArgsConstructor

public class DbUser {
	
	
	@Id
	//@GeneratedValue
	private String id;
	
	
	@NotNull
	@Column(name = "USER_NAME")
	private String userName;
	
	
	@NotNull
	@Column(name="PASSWORD")
	private String password;
	
	private String email;
	
	private String phone;

	public DbUser(String id, @NotNull String userName, @NotNull String password, String email, String phone) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.phone = phone;
	}
	
	

}
