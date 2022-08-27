package com.example.rsocketspring.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserRepository {
	private final PasswordEncoder passwordEncoder;

	private Map<String, UserDetails> userDb;

	@PostConstruct
	private void init() {
		userDb = Map.of(
				"user", User.withUsername("user").password(passwordEncoder.encode("password")).roles("USER").build(),
				"admin", User.withUsername("admin").password(passwordEncoder.encode("password")).roles("ADMIN").build(),
				"client", User.withUsername("client").password(passwordEncoder.encode("password")).roles("TRUSTED_CLIENT").build()
		);
	}

	public UserDetails findByUsername(String username) {
		return userDb.get(username);
	}
}
