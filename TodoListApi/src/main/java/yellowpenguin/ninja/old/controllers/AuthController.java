package yellowpenguin.ninja.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import yellowpenguin.ninja.dto.LoginUserRequest;
import yellowpenguin.ninja.dto.RegisterUserRequest;
import yellowpenguin.ninja.dto.TokenResponse;
import yellowpenguin.ninja.services.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService service;
	
	@PostMapping("/register")
	public ResponseEntity<TokenResponse> register(@RequestBody final RegisterUserRequest request){
		final TokenResponse token = service.register(request);
		return ResponseEntity.ok(token);
	}
	
	@PostMapping("/login")
	public ResponseEntity<TokenResponse> authenticate(@RequestBody final LoginPenguinUserRequest request){
		final TokenResponse token = service.login(request);
		return ResponseEntity.ok(token);		
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<TokenResponse> refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader){
		final TokenResponse token = service.refreshToken(authHeader);
		return ResponseEntity.ok(token);
	}
	
	

}
