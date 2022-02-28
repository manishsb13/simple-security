package com.manish.simplesecurity.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtility {

	String secretKey = "aSecretKey";
	// generateToken
	// validateToken
	// isExpiredToken

	public String generateToken(UserDetails user) {
		return Jwts.builder().setClaims(new HashMap<String, Object>()).setSubject(user.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1 * 60 * 60 * 1000))
				.signWith(SignatureAlgorithm.HS512, secretKey).compact();
	}

	public String getUserNameFromToken(String token) {
		return getClaims(token).getSubject();
	}
	
	public Date getTokenExpirationFromToken(String token) {
		return getClaims(token).getExpiration();
	}
	
	
	private Claims getClaims(String jwtToken) {
			return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken).getBody();
	}

	public boolean validateToken(String token, UserDetails user) {
		return Objects.nonNull(getTokenExpirationFromToken(token)) 
				&& getTokenExpirationFromToken(token).after(new Date()) 
				&& user.getUsername().equals(getUserNameFromToken(token));
		}
}


