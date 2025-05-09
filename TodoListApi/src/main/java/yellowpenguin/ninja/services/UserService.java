package yellowpenguin.ninja.services;

import yellowpenguin.ninja.dto.LoginUserRequest;
import yellowpenguin.ninja.dto.RegisterUserRequest;

public class UserService {
	
	public String register(RegisterUserRequest request) {
		// TODO: check unique email
		// Hash password
		// Register user
		// JWT Token response
		return null;		
	}
	
	public String login(LoginUserRequest request) {
		// TODO: validate email + password
		// Respond with JWT if successful
		return null;
	}
	
	public boolean validateToken() {
		// "message": "Unauthorized"
		return false;
	}

}
