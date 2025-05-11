package yellowpenguin.ninja.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import yellowpenguin.ninja.dto.RegisterUserRequest;
import yellowpenguin.ninja.models.User;
import yellowpenguin.ninja.repos.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	public User createUser(RegisterUserRequest request) {
		User user  = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(encoder.encode(request.getPassword()));
		user.setId(UUID.randomUUID().toString());
		user.setCreatedAt(LocalDateTime.now());
		User savedUser = repo.save(user);
		return savedUser;
	}
	
	public User findByEmail(String email) {
		return repo.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found"));
	}

}
