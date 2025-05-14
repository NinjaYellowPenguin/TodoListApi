package yellowpenguin.ninja.core.application.ports.input;

import yellowpenguin.ninja.core.application.dto.jwtoken.TokenResponse;
import yellowpenguin.ninja.core.domain.entities.PenguinUser;

public interface JWTokenService {	
	
	public TokenResponse createTokens(PenguinUser user);
	public TokenResponse refreshToken(String refreshToken, PenguinUser user);
	public void revokeToken(String token);
	public void revokeAllUsersTokens(PenguinUser user);
	public boolean validateToken(String token);
	//public String extractUsername(String refreshToken);
	//public Token findByToken(String jwtToken);
}
