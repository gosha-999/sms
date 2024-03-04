package byteblaze.sms.service;

import byteblaze.sms.model.Module;
import byteblaze.sms.model.Nutzer;
import byteblaze.sms.model.Task;
import byteblaze.sms.repository.ModuleRepository;
import byteblaze.sms.repository.NutzerRepository;
import byteblaze.sms.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ModuleRepository moduleRepository;
    private final NutzerRepository nutzerRepository;

    public Task addTaskToModule(Long moduleId, Task task) {

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));

        task.setModuleId(moduleId);
        Task savedTask = taskRepository.save(task);

        // Task-ID zur Liste der Task-IDs im Modul hinzufügen
        module.getTaskIds().add(savedTask.getId());

        // Modul mit aktualisierter Task-IDs-Liste speichern
        moduleRepository.save(module);
        return task;
    }

}

