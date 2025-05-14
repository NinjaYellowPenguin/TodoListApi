package yellowpenguin.ninja.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import yellowpenguin.ninja.dto.LoginUserRequest;
import yellowpenguin.ninja.dto.RegisterUserRequest;
import yellowpenguin.ninja.dto.TokenResponse;
import yellowpenguin.ninja.models.User;

@Service
public class AuthService {
	@Autowired
	private TaskService userService;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authManager;

	@Transactional
	public TokenResponse register(RegisterUserRequest request) {		
		User savedUser = userService.createUser(request);
		TokenResponse response = jwtService.createTokens(savedUser);		
		return response;
	}	

	public TokenResponse login(LoginPenguinUserRequest request) {
		authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
		User user  = userService.findByEmail(request.getEmail());
		TokenResponse response = jwtService.createTokens(user);
		jwtService.revokeAllUsersTokens(user);
		return response;
	}	

	public TokenResponse refreshToken(String authHeader) {
		final String refreshToken = extractRefreshToken(authHeader);		
		final String email = extractEmail(refreshToken);
		final User user = userService.findByEmail(email);		
		return jwtService.refreshToken(refreshToken, user);
	}
	
	public void logout(String token) {		
		final String jwtToken = extractRefreshToken(token);	
		jwtService.revokeToken(jwtToken);		
	}
	
	private String extractEmail(String refreshToken) {
		final String userEmail = jwtService.extractUsername(refreshToken);
		if(userEmail == null) {
			throw new IllegalArgumentException("Invalid Email Refresh Token");
		}
		return userEmail;
	}

	private String extractRefreshToken(String authHeader) {
		if(authHeader == null || !authHeader.startsWith("Bearer ")){
			throw new IllegalArgumentException("Invalid bearer token");
		}
		return authHeader.substring(7);
	}
}
