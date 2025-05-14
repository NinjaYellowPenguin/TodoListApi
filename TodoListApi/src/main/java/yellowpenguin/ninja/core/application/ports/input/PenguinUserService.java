package yellowpenguin.ninja.core.application.ports.input;

import yellowpenguin.ninja.core.application.dto.penguinuser.RegisterPenguinUserRequest;
import yellowpenguin.ninja.core.domain.entities.PenguinUser;

public interface PenguinUserService {
	
	public PenguinUser createUser(RegisterPenguinUserRequest request);	
	public PenguinUser findByEmail(String email);

}
