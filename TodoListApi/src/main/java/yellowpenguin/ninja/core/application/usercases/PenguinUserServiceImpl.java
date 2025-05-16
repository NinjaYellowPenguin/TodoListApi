package yellowpenguin.ninja.core.application.usercases;

import java.time.LocalDateTime;
import java.util.UUID;

import yellowpenguin.ninja.core.application.dto.penguinuser.RegisterPenguinUserRequest;
import yellowpenguin.ninja.core.application.ports.input.PenguinUserService;
import yellowpenguin.ninja.core.application.ports.output.PasswordEncoder;
import yellowpenguin.ninja.core.application.ports.output.repos.PenguinUserRepository;
import yellowpenguin.ninja.core.domain.entities.PenguinUser;
import yellowpenguin.ninja.core.domain.exceptions.EmailNotFoundException;

public class PenguinUserServiceImpl implements PenguinUserService{
	
	private PenguinUserRepository repo;
	private PasswordEncoder encoder;
	
	public PenguinUserServiceImpl(PenguinUserRepository repo, PasswordEncoder encoder) {
		this.repo = repo;
		this.encoder = encoder;
	}
	
	private PenguinUserServiceImpl() {
		
	}

	@Override
	public PenguinUser createUser(RegisterPenguinUserRequest request) {
		PenguinUser user = new PenguinUser();
		user.setId(UUID.randomUUID().toString());
		user.setEmail(request.getEmail());
		user.setName(request.getName());
		//TODO:Encode
		user.setPassword(encoder.encode(request.getPassword()));
		user.setCreatedAt(LocalDateTime.now());
		
		PenguinUser savedUser = repo.save(user);
		
		return savedUser;
	}

	@Override
	public PenguinUser findByEmail(String email) {
		return repo.findByEmail(email).orElseThrow(()-> new EmailNotFoundException("Email not found"));
		
	}
	

}
