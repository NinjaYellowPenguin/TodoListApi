package yellowpenguin.ninja.core.application.ports.output;

import yellowpenguin.ninja.core.application.dto.task.PaginatedTaskResponse;
import yellowpenguin.ninja.core.domain.entities.Task;

public interface TaskRepository {
	
	public Task save(Task task);
	public Task findById(String id);
	public PaginatedTaskResponse findAllUserTasks(String userId, int page, int size);

}
