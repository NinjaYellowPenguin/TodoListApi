package yellowpenguin.ninja.core.application.ports.output.repos;

import java.util.Optional;

import yellowpenguin.ninja.core.domain.entities.PenguinUser;

public interface PenguinUserRepository {
	
	public PenguinUser save(PenguinUser user);
	public Optional<PenguinUser> findById(String id);
	public Optional<PenguinUser> findByEmail(String email);

}
