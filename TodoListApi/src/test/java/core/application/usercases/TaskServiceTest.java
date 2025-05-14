package core.application.usercases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import yellowpenguin.ninja.core.application.dto.task.CreateTaskRequest;
import yellowpenguin.ninja.core.application.dto.task.TaskResponse;
import yellowpenguin.ninja.core.application.dto.task.UpdateTaskRequest;
import yellowpenguin.ninja.core.application.ports.output.TaskRepository;
import yellowpenguin.ninja.core.application.usercases.TaskServiceImpl;
import yellowpenguin.ninja.core.domain.entities.PenguinUser;
import yellowpenguin.ninja.core.domain.entities.Task;

public class TaskServiceTest {
	
	@Mock
	private TaskRepository repo;
	@InjectMocks
	private TaskServiceImpl service;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void createTest() {		
		Task task = getTestTask();		
		when(repo.save(any(Task.class))).thenReturn(task);
		
		CreateTaskRequest request = new CreateTaskRequest();
		request.setUser(getTestUser());
		request.setTitle(task.getTitle());
		request.setDescription(task.getDescription());
		
		TaskResponse response = service.create(request);
		
		assertEquals(response.getDescription(), request.getDescription());
		assertEquals(response.getId(), task.getId());
		assertEquals(response.getUserID(), task.getUser().getId());
		assertEquals(response.getTitle(), task.getTitle());		
	}
	
	@Test
	public void updateTest() {
		Task task = getTestTask();
		Task defaultTask = getTestTask();
		when(repo.findById(any(String.class))).thenReturn(task);
		when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
		
		UpdateTaskRequest request = new UpdateTaskRequest();
		request.setId(task.getId());
		request.setDescription("Comer MUCHOS peces");
		request.setTitle(request.getTitle());
		
		TaskResponse response = service.update(request);
		
		assertNotEquals(response.getDescription(), defaultTask.getDescription());
		assertEquals(response.getTitle(), task.getTitle());
		assertEquals(response.getId(), request.getId());
		assertEquals(response.getUserID(), task.getUser().getId());		
	}
	
	@Test
	public void findPaginatedTasksTest() {
		
		//No Logic implemented on Service.
		
	}
	
	private Task getTestTask() {
		Task task = new Task();
		task.setId("testId");
		task.setCreatedAt(LocalDateTime.now());
		task.setTitle("Cosas de peces");
		task.setDescription("Comer peces");
		task.setUser(getTestUser());
		return task;
	}
	
	private PenguinUser getTestUser() {
		PenguinUser penguin = new PenguinUser();
		penguin.setId("idAlfred");
		penguin.setName("Alfred");
		penguin.setEmail("ilovefish@yellowpenguin.ninja");
		penguin.setPassword("encryptedPassword");
		penguin.setCreatedAt(LocalDateTime.now());
		return penguin;
	}
}
