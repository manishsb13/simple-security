package com.manish.simplesecurity.config;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.manish.simplesecurity.model.DbUser;
import com.manish.simplesecurity.repository.UserRepository;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

	@Autowired
	UserRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//load user from db
		Optional<DbUser> optional = repo.findByUserName(username);
		if(optional.isPresent()) {
			DbUser user = optional.get();
			return new User(user.getUserName(), user.getPassword(), new ArrayList<>());
		}
		throw new UsernameNotFoundException(MessageFormat.format("Username {0} not found in the db", username));
	}

}
