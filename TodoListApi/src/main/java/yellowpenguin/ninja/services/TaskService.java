package yellowpenguin.ninja.services;

import java.util.List;

import yellowpenguin.ninja.dto.CreateTaskRequest;
import yellowpenguin.ninja.dto.TaskResponse;
import yellowpenguin.ninja.dto.UpdateTaskRequest;

public class TaskService {
	
	public TaskResponse create(CreateTaskRequest request) {
		// TODO: Must check auth token before create (in controller)
		// Save task into db
		return null;
	}
	
	public TaskResponse update(UpdateTaskRequest request) {
		// TODO: Must check auth token before create (in controller)
		// Save task into db
		return null;
	}
	
	public List<TaskResponse> findPaginatedTasks(Object request) {
		// TODO: Must check auth token before create (in controller)
		// Check info
		return null;
	}

}
