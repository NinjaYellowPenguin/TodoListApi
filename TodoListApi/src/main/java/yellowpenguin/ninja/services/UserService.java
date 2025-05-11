package yellowpenguin.ninja.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import yellowpenguin.ninja.dto.LoginUserRequest;
import yellowpenguin.ninja.dto.RegisterUserRequest;
import yellowpenguin.ninja.models.User;
import yellowpenguin.ninja.repos.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repo;
	
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
	
	public User findByEmail(String email) {
		return repo.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found"));
	}

}
