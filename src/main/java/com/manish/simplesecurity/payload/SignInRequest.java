package com.manish.simplesecurity.payload;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class SignInRequest {

	@NotNull
	private String userName;
	
	@NotNull
	private String password;
}
