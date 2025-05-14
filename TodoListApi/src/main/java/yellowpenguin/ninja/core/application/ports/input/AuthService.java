package yellowpenguin.ninja.core.application.ports.input;

import yellowpenguin.ninja.core.application.dto.jwtoken.TokenResponse;
import yellowpenguin.ninja.core.application.dto.penguinuser.LoginPenguinUserRequest;
import yellowpenguin.ninja.core.application.dto.penguinuser.RegisterPenguinUserRequest;

public interface AuthService {
	
	public TokenResponse register(RegisterPenguinUserRequest request);
	public TokenResponse login(LoginPenguinUserRequest request);
	public TokenResponse refreshToken(String authHeader);	
	public void logout(String token);	

}
