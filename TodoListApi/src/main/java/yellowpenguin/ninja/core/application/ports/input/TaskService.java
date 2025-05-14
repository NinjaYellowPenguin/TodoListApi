package yellowpenguin.ninja.core.application.ports.input;

import java.util.List;

import yellowpenguin.ninja.core.application.dto.task.CreateTaskRequest;
import yellowpenguin.ninja.core.application.dto.task.TaskResponse;
import yellowpenguin.ninja.core.application.dto.task.UpdateTaskRequest;
import yellowpenguin.ninja.core.domain.entities.PenguinUser;

public interface TaskService {
	
	public TaskResponse create(CreateTaskRequest request);
	public TaskResponse update(UpdateTaskRequest request);	
	public List<TaskResponse> findPaginatedTasks(Object request);
	public void findAllByUser(PenguinUser user);

}
