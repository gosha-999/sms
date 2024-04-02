package byteblaze.sms.repository;

import byteblaze.sms.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;


public interface TaskRepository extends JpaRepository<Task, Long> {
    boolean existsByModuleId(Long moduleId);

    List<Task> findByNutzerId(Long nutzerId);
    List<Task> findByModuleId(Long moduleId);
    List<Task> findByModuleIdAndNutzerId(Long moduleId, Long nutzerId);
    Task findByIdAndNutzerId(Long taskId, Long nutzerId);
}

