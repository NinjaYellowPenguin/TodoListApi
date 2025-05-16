package yellowpenguin.ninja.core.application.usercases;

import java.util.Date;
import java.util.List;

import yellowpenguin.ninja.core.application.dto.jwtoken.JWTokenClaims;
import yellowpenguin.ninja.core.application.dto.jwtoken.TokenResponse;
import yellowpenguin.ninja.core.application.ports.input.JWTokenService;
import yellowpenguin.ninja.core.application.ports.output.JWTokenManager;
import yellowpenguin.ninja.core.application.ports.output.repos.JWTokenRepository;
import yellowpenguin.ninja.core.domain.entities.JWToken;
import yellowpenguin.ninja.core.domain.entities.JWToken.TokenType;
import yellowpenguin.ninja.core.domain.entities.PenguinUser;

public class JWTokenServiceImpl implements JWTokenService {

	private JWTokenRepository repo;

	private JWTokenManager tokenManager;

	@Override
	public TokenResponse createTokens(PenguinUser user) {

		String accessToken = tokenManager.generateAccessToken(user);
		String refreshToken = tokenManager.generateRefreshToken(user);
		saveToken(user, accessToken);
		saveToken(user, refreshToken);
		return createTokenResponse(accessToken, refreshToken);
	}

	@Override
	public TokenResponse refreshToken(String refreshToken, PenguinUser user) {
		if (validateToken(refreshToken)) {
			throw new IllegalArgumentException("Invalid User Refresh Token");
		}
		final String accessToken = tokenManager.generateAccessToken(user);
		// revokeAllUsersTokens(user); - only revoke accesstokens
		saveToken(user, accessToken);
		TokenResponse response = new TokenResponse();
		response.setAccessToken(accessToken);
		response.setRefreshToken(refreshToken);
		return response;
	}

	@Override
	public void revokeToken(String token) {
		JWToken foundToken = repo.findByToken(token).orElseThrow(() -> new IllegalArgumentException("Invalid Token"));
		foundToken.setExpired(true);
		foundToken.setRevoked(true);
		repo.save(foundToken);

	}

	@Override
	public void revokeAllUsersTokens(PenguinUser user) {
		List<JWToken> validUserTokens = repo.findAllValidIsFalseOrRevokedIsFalseByUserId(user.getId());
		if (!validUserTokens.isEmpty()) {
			for (JWToken token : validUserTokens) {
				token.setRevoked(true);
			}
			repo.saveAll(validUserTokens);
		}
	}

	@Override
	public boolean validateToken(String token) {		
		if(!checkTokenIntegration(token)) {
			throw new IllegalArgumentException("Invalid Token");
		}
		JWToken jwToken = repo.findByToken(token).orElseThrow(() -> new IllegalArgumentException("Invalid Token"));
		if(!checkTokenExpiration(jwToken)) {
			throw new IllegalArgumentException("Expired Token");
		}
		if(!checkTokenRevokation(jwToken)) {
			throw new IllegalArgumentException("Revoked Token");
		}		
		return true;		
	}
	
	private boolean checkTokenIntegration(String token) {
		if(!tokenManager.isTokenValid(token)) {
			return false;
		}
		return true;
	}
	
	private boolean checkTokenExpiration(JWToken jwToken) {
		JWTokenClaims claims = tokenManager.extractClaims(jwToken.getToken());
		if(claims.getExpiration().before(new Date())) {
			jwToken.setExpired(true);
			repo.save(jwToken);
			return false;
		}
		return true;
	}
	
	private boolean checkTokenRevokation(JWToken jwToken) {
		if(jwToken.isRevoked()) {
			return false;
		}		
		return true;		
	}


	private TokenResponse createTokenResponse(String accessToken, String refreshToken) {
		TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setAccessToken(accessToken);
		tokenResponse.setRefreshToken(refreshToken);
		return tokenResponse;
	}

	private void saveToken(PenguinUser user, String tokenString) {
		JWToken token = new JWToken();
		token.setUser(user);
		token.setToken(tokenString);
		token.setTokenType(TokenType.BEARER);
		token.setExpired(false);
		token.setRevoked(false);
		repo.save(token);
	}

}
