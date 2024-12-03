package com.sparta.msa_exam.auth.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.msa_exam.auth.dto.AuthResponse;
import com.sparta.msa_exam.auth.dto.SignInRequest;
import com.sparta.msa_exam.auth.entity.User;
import com.sparta.msa_exam.auth.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/auth/sign-in")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody SignInRequest signInRequest) {
		String token = authService.signIn(signInRequest.getUserId(), signInRequest.getPassword());
		return ResponseEntity.ok(new AuthResponse(token));
	}

	@PostMapping("/auth/sign-up")
	public ResponseEntity<?> signUp(@RequestBody User user) {
		log.info("Sign up request received ####User" + user);
		User createdUser = authService.signUp(user);
		return ResponseEntity.ok(createdUser);
	}
}
