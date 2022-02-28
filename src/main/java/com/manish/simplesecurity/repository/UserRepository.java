package com.manish.simplesecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manish.simplesecurity.model.DbUser;

@Repository
public interface UserRepository extends JpaRepository<DbUser, String> {

	Optional<DbUser> findByUserName(String userName);
	boolean existsByUserName(String userName );
	boolean existsByEmail(String email);
}
