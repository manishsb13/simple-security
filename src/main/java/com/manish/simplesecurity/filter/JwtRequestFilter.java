package com.manish.simplesecurity.filter;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.manish.simplesecurity.config.UserDetailsServiceImp;
import com.manish.simplesecurity.utils.JwtUtility;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	JwtUtility jwtUtils;
	
	@Autowired
	UserDetailsServiceImp userService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//read jwt token from authorization header
		if(Objects.nonNull(request.getHeader("Authorization")) && SecurityContextHolder.getContext().getAuthentication() == null){
			String jwtToken = request.getHeader("Authorization");
			String userName = jwtUtils.getUserNameFromToken(jwtToken);
			UserDetails user = userService.loadUserByUsername(userName);
			if(jwtUtils.validateToken(jwtToken, user)) {
				UsernamePasswordAuthenticationToken authhTOken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				
				authhTOken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authhTOken);	
			}
			
			
		}
		//get user, validate token, user and expiration of token
		//if valid then update the usernamepasswordauthtoken in securitycontextholder
		//filter request
		filterChain.doFilter(request, response);
		
	}

}
