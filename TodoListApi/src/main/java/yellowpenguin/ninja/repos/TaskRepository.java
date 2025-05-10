package yellowpenguin.ninja.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yellowpenguin.ninja.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, String>{

}
