package yellowpenguin.ninja.core.application.ports.output;

import yellowpenguin.ninja.core.domain.entities.Task;

public interface TaskRepository {
	
	public Task save(Task task);
	public Task findById(String id);

}
