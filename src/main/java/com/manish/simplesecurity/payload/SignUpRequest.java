package com.manish.simplesecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class SignUpRequest {
	
	private String id;
	private String userName;
	private String password;
	private String email;
	private String phone;
	
}
