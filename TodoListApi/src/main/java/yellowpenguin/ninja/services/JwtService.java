package yellowpenguin.ninja.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import yellowpenguin.ninja.dto.TokenResponse;
import yellowpenguin.ninja.models.Token;
import yellowpenguin.ninja.models.User;
import yellowpenguin.ninja.repos.TokenRepository;
import yellowpenguin.ninja.models.Token.TokenType;

@Service
public class JwtService {
	
	@Value("${jwt.secret}")
	private String secretKey;
	@Value("${jwt.expiration-time}")
	private long jwtExpiration;
	@Value("${jwt.refresh-token-expiration-time}")
	private long jwtRefresh;
	
	@Autowired
	private TokenRepository repo;
	
	public TokenResponse createTokens(User user) {
		String token = generateToken(user);
		String refreshToken = generateRefreshToken(user);
		saveToken(user, token);
		
		TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setAccessToken(token);
		tokenResponse.setRefreshToken(refreshToken);
		return tokenResponse;
	}
	
	private void saveToken(User user, String tokenString) {
		Token token = new Token();
		token.setUser(user);
		token.setToken(tokenString);
		token.setTokenType(TokenType.BEARER);
		token.setExpired(false);
		token.setRevoked(false);
		repo.save(token);
	}
	
	private String generateToken(User savedUser) {
		return buildToken(savedUser, jwtExpiration);
	}
	private String generateRefreshToken(User savedUser) {
		return buildToken(savedUser, jwtRefresh);
	}

	private String buildToken(User savedUser, long expiration) {		
		return Jwts.builder()
		.id(savedUser.getId())
		.claims(Map.of("name", savedUser.getName()))
		.subject(savedUser.getEmail())
		.issuedAt(new Date(System.currentTimeMillis()))
		.expiration(new Date(System.currentTimeMillis() + expiration))
		.signWith(getSignInKey())
		.compact();		
	}
	
	@Transactional
	public TokenResponse refreshToken(String refreshToken, User user) {
		if(!isTokenValid(refreshToken, user)) {
			throw new IllegalArgumentException("Invalid User Refresh Token");
		}
		final String accessToken = generateToken(user);
		revokeAllUsersTokens(user);
		saveToken(user, accessToken);		
		TokenResponse response = new TokenResponse();
		response.setAccessToken(accessToken);
		response.setRefreshToken(refreshToken);
		return response;
	}
	
	public void revokeToken(String token) {
		Token foundToken = repo.findByToken(token).orElseThrow(()->new IllegalArgumentException("Invalid Token Logout."));		
		foundToken.setExpired(true);
		foundToken.setRevoked(true);
		repo.save(foundToken);		
	}
	
	public void revokeAllUsersTokens(User user) {
		List<Token> validUserTokens = repo.findAllValidIsFalseOrRevokedIsFalseByUserId(user.getId());
		if(!validUserTokens.isEmpty()) {
			for(Token token: validUserTokens) {
				token.setExpired(true);
				token.setRevoked(true);
			}
			repo.saveAll(validUserTokens);
		}		
	}
	
	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	public String extractUsername(String refreshToken) {
		final Claims jwtToken = Jwts.parser()
				.verifyWith(getSignInKey())
				.build()
				.parseSignedClaims(refreshToken)
				.getPayload();
		return jwtToken.getSubject();
	}
	public boolean isTokenValid(String refreshToken, User user) {
		final String userName = extractUsername(refreshToken);
		return (userName.equals(user.getEmail()) && !isTokenExpired(refreshToken));
	}
	private boolean isTokenExpired(String refreshToken) {
		final Claims jwtToken = Jwts.parser()
				.verifyWith(getSignInKey())
				.build()
				.parseSignedClaims(refreshToken)
				.getPayload();
		return jwtToken.getExpiration().before(new Date());
	}

	
}
