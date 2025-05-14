package yellowpenguin.ninja.core.application.ports.output;

import yellowpenguin.ninja.core.domain.entities.PenguinUser;

public interface PenguinUserRepository {
	
	public PenguinUser save(PenguinUser user);
	public PenguinUser findById(String id);
	public PenguinUser findByEmail(String email);

}
