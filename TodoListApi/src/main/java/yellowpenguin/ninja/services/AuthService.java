package yellowpenguin.ninja.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import yellowpenguin.ninja.dto.LoginUserRequest;
import yellowpenguin.ninja.dto.RegisterUserRequest;
import yellowpenguin.ninja.dto.TokenResponse;
import yellowpenguin.ninja.models.Token;
import yellowpenguin.ninja.models.Token.TokenType;
import yellowpenguin.ninja.models.User;
import yellowpenguin.ninja.repos.TokenRepository;

@Service
public class AuthService {
	@Autowired
	private UserService userService;
	@Autowired
	private TokenRepository tokenRepo;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authManager;

	@Transactional
	public TokenResponse register(RegisterUserRequest request) {
		
		User savedUser = userService.createUser(request);
		
		String token = jwtService.generateToken(savedUser);
		String refreshToken = jwtService.generateRefreshToken(savedUser);
		saveToken(savedUser, token);
		
		TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setAccessToken(token);
		tokenResponse.setRefreshToken(refreshToken);
		return tokenResponse;
	}

	private void saveToken(User savedUser, String tokenString) {
		Token token = new Token();
		token.setUser(savedUser);
		token.setToken(tokenString);
		token.setTokenType(TokenType.BEARER);
		token.setExpired(false);
		token.setRevoked(false);
		tokenRepo.save(token);
	}

	public TokenResponse login(LoginUserRequest request) {
		authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
		User user  = userService.findByEmail(request.getEmail());
		String token = jwtService.generateToken(user);
		String refreshToken = jwtService.generateRefreshToken(user);
		TokenResponse response = new TokenResponse();
		response.setAccessToken(token);
		response.setRefreshToken(refreshToken);
		revokeAllUsersTokens(user);
		return response;
	}

	private void revokeAllUsersTokens(User user) {
		List<Token> validUserTokens = tokenRepo.findAllValidIsFalseOrRevokedIsFalseByUserId(user.getId());
		if(!validUserTokens.isEmpty()) {
			for(Token token: validUserTokens) {
				token.setExpired(true);
				token.setRevoked(true);
			}
			tokenRepo.saveAll(validUserTokens);
		}		
	}

	public TokenResponse refreshToken(String authHeader) {
		if(authHeader == null || !authHeader.startsWith("Bearer ")){
			throw new IllegalArgumentException("Invalid bearer token");
		}
		final String refreshToken = authHeader.substring(7);
		final String userEmail = jwtService.extractUsername(refreshToken);
		if(userEmail == null) {
			throw new IllegalArgumentException("Invalid Email Refresh Token");
		}
		final User user = userService.findByEmail(userEmail);
		
		if(!jwtService.isTokenValid(refreshToken, user)) {
			throw new IllegalArgumentException("Invalid Refresh Token");
		}
		final String accessToken = jwtService.generateToken(user);
		revokeAllUsersTokens(user);
		saveToken(user, accessToken);
		
		TokenResponse response = new TokenResponse();
		response.setAccessToken(accessToken);
		response.setRefreshToken(refreshToken);
		return response;
	}
	
	public void logout(String token) {
		
		if(token == null || !token.startsWith("Bearer ")) {
			throw new IllegalArgumentException("Invalid Token");		}
		
		final String jwtToken = token.substring(7);		
		final Token foundToken = tokenRepo.findByToken(jwtToken).orElseThrow(()->new IllegalArgumentException("Invalid Token Logout."));		
		foundToken.setExpired(true);
		foundToken.setRevoked(true);
		tokenRepo.save(foundToken);
		
	}


}
