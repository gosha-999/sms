package byteblaze.sms.repository;

import byteblaze.sms.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Long> {
}

