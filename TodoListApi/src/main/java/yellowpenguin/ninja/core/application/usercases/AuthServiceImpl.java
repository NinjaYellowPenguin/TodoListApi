package yellowpenguin.ninja.core.application.usercases;

import yellowpenguin.ninja.core.application.dto.jwtoken.TokenResponse;
import yellowpenguin.ninja.core.application.dto.penguinuser.LoginPenguinUserRequest;
import yellowpenguin.ninja.core.application.dto.penguinuser.RegisterPenguinUserRequest;
import yellowpenguin.ninja.core.application.ports.input.AuthService;

public class AuthServiceImpl implements AuthService{

	@Override
	public TokenResponse register(RegisterPenguinUserRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TokenResponse login(LoginPenguinUserRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TokenResponse refreshToken(String authHeader) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void logout(String token) {
		// TODO Auto-generated method stub
		
	}

}
