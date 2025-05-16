package yellowpenguin.ninja.infrastructure.adapters.output.auth;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import yellowpenguin.ninja.core.application.dto.jwtoken.JWTokenClaims;
import yellowpenguin.ninja.core.application.ports.output.JWTokenManager;
import yellowpenguin.ninja.core.domain.entities.PenguinUser;

public class JWTokenManagerAdapter implements JWTokenManager{
	
	@Value("${jwt.secret}")
	private String secretKey;
	@Value("${jwt.expiration-time}")
	private long jwtExpiration;
	@Value("${jwt.refresh-token-expiration-time}")
	private long jwtRefresh;

	@Override
	public String generateAccessToken(PenguinUser savedUser){
		return buildToken(savedUser, jwtExpiration);
	}

	@Override
	public String generateRefreshToken(PenguinUser savedUser){
		return buildToken(savedUser, jwtRefresh);
	}
	
	@Override
	public boolean isTokenValid(String token) {
		try {
			Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public JWTokenClaims extractClaims(String token) {
		Claims claims =Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
		
		JWTokenClaims claimsDto = new JWTokenClaims();
		claimsDto.setEmail(claims.getSubject());
		claimsDto.setExpiration(claims.getExpiration());
		claimsDto.setId(claims.getId());
		claimsDto.setName(claims.get("name").toString());
		return claimsDto;
	}
	
	

	private String buildToken(PenguinUser savedUser, long expiration) {
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

	

	

	



}
