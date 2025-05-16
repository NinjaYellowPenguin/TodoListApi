package core.application.usercases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import yellowpenguin.ninja.core.application.dto.penguinuser.RegisterPenguinUserRequest;
import yellowpenguin.ninja.core.application.ports.output.PasswordEncoder;
import yellowpenguin.ninja.core.application.ports.output.repos.PenguinUserRepository;
import yellowpenguin.ninja.core.application.usercases.PenguinUserServiceImpl;
import yellowpenguin.ninja.core.domain.entities.PenguinUser;
import yellowpenguin.ninja.core.domain.exceptions.EmailNotFoundException;

public class PenguinUserServiceImplTest {
	
	@Mock
	private PenguinUserRepository repo;
	@Mock
	private PasswordEncoder encoder;
	@InjectMocks
	private PenguinUserServiceImpl service;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void createUserTest() {
		
		when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
		when(encoder.encode(any())).thenAnswer(invocation -> invocation.getArgument(0)+"2");
		
		RegisterPenguinUserRequest request = new RegisterPenguinUserRequest();
		request.setEmail("litos@yellowpenguin.ninja");
		request.setName("Alfred");
		request.setPassword("password");		
		
		PenguinUser user = service.createUser(request);
		
		assertEquals(user.getEmail(), request.getEmail());
		assertEquals(user.getName(), request.getName());
		assertEquals(user.getPassword(), request.getPassword() + "2");
		
		assertNotEquals(user.getPassword(), request.getPassword());
		
		assertNotNull(user.getCreatedAt());
		assertNotNull(user.getId());
		assertNull(user.getTokens());		
		assertNull(user.getUpdatedAt());		
	}
	
	@Test
	public void findByEmailTest() {
		PenguinUser defaultUser = getTestUser();
		Optional<PenguinUser> fullOptional = Optional.of(defaultUser);		
		when(repo.findByEmail(defaultUser.getEmail())).thenReturn(fullOptional);
		
		
		PenguinUser user = service.findByEmail(defaultUser.getEmail());
		assertEquals(user.getId(), defaultUser.getId());
		assertEquals(user.getCreatedAt(), defaultUser.getCreatedAt());
		assertEquals(user.getEmail(), defaultUser.getEmail());
		assertEquals(user.getName(), defaultUser.getName());
		assertEquals(user.getPassword(), defaultUser.getPassword());
		assertEquals(user.getTokens(), defaultUser.getTokens());
		assertEquals(user.getUpdatedAt(), defaultUser.getUpdatedAt());
	}
	
	@Test
	public void findByEmailExceptionTest() {
		Optional<PenguinUser> voidOptional = Optional.empty();
		when(repo.findByEmail("")).thenReturn(voidOptional);
		
		 assertThrows(EmailNotFoundException.class, () -> {
	            service.findByEmail("");
	        });
	}
	
	private PenguinUser getTestUser() {
		PenguinUser penguin = new PenguinUser();
		penguin.setId("idAlfred");
		penguin.setName("Alfred");
		penguin.setEmail("ilovefish@yellowpenguin.ninja");
		penguin.setPassword("encryptedPassword");
		penguin.setCreatedAt(LocalDateTime.now());
		//penguin.setUpdatedAt(LocalDateTime.now());
		return penguin;
	}

}
