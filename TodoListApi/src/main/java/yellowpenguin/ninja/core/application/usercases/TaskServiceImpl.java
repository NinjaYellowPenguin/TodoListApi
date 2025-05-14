package yellowpenguin.ninja.core.application.usercases;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import yellowpenguin.ninja.core.application.dto.task.CreateTaskRequest;
import yellowpenguin.ninja.core.application.dto.task.TaskResponse;
import yellowpenguin.ninja.core.application.dto.task.UpdateTaskRequest;
import yellowpenguin.ninja.core.application.ports.input.TaskService;
import yellowpenguin.ninja.core.application.ports.output.TaskRepository;
import yellowpenguin.ninja.core.domain.entities.PenguinUser;
import yellowpenguin.ninja.core.domain.entities.Task;

public class TaskServiceImpl implements TaskService{
	
	private TaskRepository repo;
	
	public TaskServiceImpl(TaskRepository repo) {
		this.repo = repo;
	}

	@Override
	public TaskResponse create(CreateTaskRequest request) {
		Task task = new Task();
		task.setId(UUID.randomUUID().toString());
		task.setCreatedAt(LocalDateTime.now());
		task.setTitle(request.getTitle());
		task.setDescription(request.getDescription());		
		task.setUser(request.getUser());
		
		repo.save(task);
		
		return createTaskResponse(task);
	}

	@Override
	public TaskResponse update(UpdateTaskRequest request) {
		Task task = repo.findById(request.getId());
		task.setUpdatedAt(LocalDateTime.now());		
		task.setTitle(request.getTitle());
		task.setDescription(request.getDescription());
		task = repo.save(task);
		return createTaskResponse(task);
	}

	@Override
	public List<TaskResponse> findPaginatedTasks(Object request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void findAllByUser(PenguinUser user) {
		// TODO Auto-generated method stub
		
	}
	
	private TaskResponse createTaskResponse(Task task) {
		TaskResponse response = new TaskResponse();
		response.setId(task.getId());
		response.setUserID(task.getUser().getId());
		response.setTitle(task.getTitle());
		response.setDescription(task.getDescription());
		return response;		
	}

}
