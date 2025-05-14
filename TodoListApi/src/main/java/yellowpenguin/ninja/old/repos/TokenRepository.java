package yellowpenguin.ninja.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import yellowpenguin.ninja.models.Token;

public interface TokenRepository extends JpaRepository<Token, Long>{

	List<Token> findAllValidIsFalseOrRevokedIsFalseByUserId(String id);

	Optional<Token> findByToken(String jwtToken);
	
	

}
