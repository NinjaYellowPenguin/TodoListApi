package yellowpenguin.ninja.core.application.ports.output.repos;

import java.util.List;

import yellowpenguin.ninja.core.domain.entities.JWToken;

public interface JWTokenRepository {
	
	public JWToken save(JWToken jwToken);
	public JWToken findByToken(String token);
	public List<JWToken> findAllValidIsFalseOrRevokedIsFalseByUserId(String userId);

}
