package yellowpenguin.ninja.core.application.ports.input;

import yellowpenguin.ninja.core.application.dto.task.CreateTaskRequest;
import yellowpenguin.ninja.core.application.dto.task.PaginatedTaskRequest;
import yellowpenguin.ninja.core.application.dto.task.PaginatedTaskResponse;
import yellowpenguin.ninja.core.application.dto.task.TaskResponse;
import yellowpenguin.ninja.core.application.dto.task.UpdateTaskRequest;

public interface TaskService {	
	
	public TaskResponse create(CreateTaskRequest request);
	public TaskResponse update(UpdateTaskRequest request);	
	public PaginatedTaskResponse findPaginatedTasks(PaginatedTaskRequest request);
	
}
