package com.manish.simplesecurity.controller;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.manish.simplesecurity.config.UserDetailsServiceImp;
import com.manish.simplesecurity.exception.UserAlreadyPresentException;
import com.manish.simplesecurity.exception.UserNotFoundException;
import com.manish.simplesecurity.model.DbUser;
import com.manish.simplesecurity.payload.ErrorMessage;
import com.manish.simplesecurity.payload.FieldErrorMsg;
import com.manish.simplesecurity.payload.SignInRequest;
import com.manish.simplesecurity.payload.SignInResponse;
import com.manish.simplesecurity.payload.SignUpRequest;
import com.manish.simplesecurity.repository.UserRepository;
import com.manish.simplesecurity.utils.JwtUtility;

@RestController
public class UserController {

	@Autowired
	UserRepository repo;
	
	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
	UserDetailsServiceImp useService;
	
	@Autowired
	JwtUtility jwtutils;
	
	@Autowired
	PasswordEncoder encoder;
	
	
	@PostMapping("/api/user/register")
	public SignUpRequest register(@Valid @RequestBody SignUpRequest request) throws UserAlreadyPresentException {
		
		//validate if user is not present already
		String userName = request.getUserName();
		String email = request.getEmail();
		
		//insert in db, if new user		
		if(!repo.existsByUserName(userName) && !repo.existsByEmail(email)) {
			DbUser user = repo.save(new DbUser(UUID.randomUUID().toString(), request.getUserName(), encoder.encode(request.getPassword()), request.getEmail(), request.getPhone()));
			return new SignUpRequest(user.getId().toString(),user.getUserName(), user.getPassword(), user.getEmail(), user.getPhone());
		}else
			throw new UserAlreadyPresentException("User Name or EMail is already used."); 
		
	}
	
	
	
	@GetMapping("/api/user/authenticate")
	public SignInResponse authenticate(@Valid @RequestBody SignInRequest signInRequest) throws Exception {
		//authenticate using authenticationManager
		try {
			UsernamePasswordAuthenticationToken authToken  = new UsernamePasswordAuthenticationToken(signInRequest.getUserName(), signInRequest.getPassword());
			Authentication auth = authManager.authenticate(authToken);
			System.out.println(auth);
		}catch (Exception e) {
			throw new BadCredentialsException(MessageFormat.format("Authentication failed for user {0}" ,signInRequest.getUserName()),e);
			
		}
		
		
		//create jwtToken and return
		UserDetails user = useService.loadUserByUsername(signInRequest.getUserName());
		String jwtToken = jwtutils.generateToken(user);
		return new SignInResponse("Success", jwtToken);
		
	}
	
	

	@GetMapping("/api/users/{uuid}")
	public SignUpRequest getUser(@PathVariable String uuid) throws UserNotFoundException {
		//this api call needs jwtToken in auth header
		//needs a onceperrequestfilter at the front where token is validated and user is checked in db
		Optional<DbUser> opt = repo.findById(uuid);
		if(opt.isPresent()) {
			DbUser user = opt.get();
			return new SignUpRequest(user.getId(),user.getUserName(), user.getPassword(), user.getEmail(), user.getPhone());
		}
		throw new UserNotFoundException(MessageFormat.format("User with UUID {0} not found in the system.", uuid));
	}
	

	@PostMapping("/api/users/{uuid}")
	public SignUpRequest updateUser(@RequestBody SignUpRequest updatedUser, @PathVariable String uuid) throws UserNotFoundException {
		//this api call needs jwtToken in auth header
		//needs a onceperrequestfilter at the front where token is validated and user is checked in db
		if(repo.existsById(uuid)) {			
			DbUser savedUser = repo.save(new DbUser(uuid, updatedUser.getUserName(), encoder.encode(updatedUser.getPassword()), updatedUser.getEmail(), updatedUser.getPhone()));
			return new SignUpRequest(savedUser.getId(),savedUser.getUserName(), savedUser.getPassword(), savedUser.getEmail(), savedUser.getPhone());
		}
		throw new UserNotFoundException(MessageFormat.format("User with UUID {0} not found in the system.", uuid));
		
		
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({UserNotFoundException.class , UserAlreadyPresentException.class, BadCredentialsException.class})
	public ErrorMessage handleException(Exception e) {
		return new ErrorMessage(HttpStatus.BAD_REQUEST,e.getMessage());
	}

	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({MethodArgumentNotValidException.class })
	public List<FieldErrorMsg> handleException1(MethodArgumentNotValidException e) {
		return e.getBindingResult().getFieldErrors()
				.stream().map(fieldError -> new FieldErrorMsg(fieldError.getField(), fieldError.getDefaultMessage()))
				.collect(Collectors.toList());
	}

	
	
}
