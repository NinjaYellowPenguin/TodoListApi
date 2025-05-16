package yellowpenguin.ninja.core.application.ports.output.repos;

import java.util.List;
import java.util.Optional;

import yellowpenguin.ninja.core.domain.entities.JWToken;

public interface JWTokenRepository {
	
	public JWToken save(JWToken jwToken);
	public Optional<JWToken> findByToken(String token);
	public List<JWToken> findAllValidIsFalseOrRevokedIsFalseByUserId(String userId);
	public void saveAll(List<JWToken> validUserTokens);

}
