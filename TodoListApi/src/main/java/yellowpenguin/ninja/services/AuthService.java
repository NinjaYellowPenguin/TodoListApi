package yellowpenguin.ninja.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import yellowpenguin.ninja.dto.LoginUserRequest;
import yellowpenguin.ninja.dto.RegisterUserRequest;
import yellowpenguin.ninja.dto.TokenResponse;
import yellowpenguin.ninja.models.Token;
import yellowpenguin.ninja.models.Token.TokenType;
import yellowpenguin.ninja.models.User;
import yellowpenguin.ninja.repos.TokenRepository;
import yellowpenguin.ninja.repos.UserRepository;

@Service
public class AuthService {
	
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private UserRepository repo;
	@Autowired
	private TokenRepository tokenRepo;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authManager;

	@Transactional
	public TokenResponse register(RegisterUserRequest request) {
		User user  = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(encoder.encode(request.getPassword()));
		user.setId(UUID.randomUUID().toString());
		user.setCreatedAt(LocalDateTime.now());
		
		User savedUser = repo.save(user);
		
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
		User user  = repo.findByEmail(request.getEmail()).orElseThrow();
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
		final User user = repo.findByEmail(userEmail).orElseThrow(()-> new UsernameNotFoundException(userEmail));
		
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
