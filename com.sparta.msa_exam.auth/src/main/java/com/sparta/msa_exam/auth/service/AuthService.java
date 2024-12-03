package com.sparta.msa_exam.auth.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sparta.msa_exam.auth.entity.User;
import com.sparta.msa_exam.auth.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService {

	@Value("${spring.application.name}")
	private String issuer;

	@Value("${service.jwt.access-expiration}")
	private Long accessExpiration;

	private final SecretKey secretKey;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthService(@Value("${service.jwt.secret-key}") String secretKey,
		UserRepository userRepository,
		PasswordEncoder passwordEncoder) {
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public String createAccessToken(String userId, String role) {
		return Jwts.builder()
			.claim("user_id", userId)
			.claim("role", role)
			.issuer(issuer)
			.issuedAt(new Date(System.currentTimeMillis()))
			.expiration(new Date(System.currentTimeMillis() + accessExpiration))
			.signWith(secretKey, io.jsonwebtoken.SignatureAlgorithm.HS512)
			.compact();
	}

	public User signUp(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("User");
		return userRepository.save(user);
	}

	public String signIn(String userId, String password) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("Invalid user ID or password"));

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException("Invalid user ID or password");
		}
		log.info("");
		return createAccessToken(user.getUserId(), user.getRole());
	}
}