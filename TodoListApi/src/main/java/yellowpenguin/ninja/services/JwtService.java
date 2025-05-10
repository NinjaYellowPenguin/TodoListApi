package yellowpenguin.ninja.services;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import yellowpenguin.ninja.models.User;

@Service
public class JwtService {
	
	@Value("${jwt.secret}")
	private String secretKey;
	@Value("${jwt.expiration-time}")
	private long jwtExpiration;
	@Value("${jwt.refresh-token-expiration-time}")
	private long jwtRefresh;
	
	public String generateToken(User savedUser) {
		return buildToken(savedUser, jwtExpiration);
	}
	public String generateRefreshToken(User savedUser) {
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
