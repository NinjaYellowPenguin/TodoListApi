package yellowpenguin.ninja.core.application.ports.output;

import yellowpenguin.ninja.core.application.dto.jwtoken.JWTokenClaims;
import yellowpenguin.ninja.core.domain.entities.PenguinUser;

public interface JWTokenManager {
	
	public String generateAccessToken(PenguinUser user);
	public String generateRefreshToken(PenguinUser user);
	public boolean isTokenValid(String token);
	public JWTokenClaims extractClaims(String token);
	
}
