package com.manish.simplesecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class SignInResponse {

	private String message;
	private String jwtToken;
}
