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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ModuleRepository moduleRepository;
    private final NutzerRepository nutzerRepository;


    //NUTZER

    //GET TaskIds
    public List<Long> getTaskIdsByNutzerId(Long nutzerId) {
        Nutzer nutzer = nutzerRepository.findById(nutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        return nutzer.getTaskIds();
    }


    //GET alle Tasks eines Nutzers als Objekt
    public List<Task> getTasksByNutzerId(Long nutzerId) {
        Nutzer nutzer = nutzerRepository.findById(nutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        List<Long> taskIds = nutzer.getTaskIds();
        List<Task> tasks = new ArrayList<>();

        for (Long taskId : taskIds) {
            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task nicht gefunden"));
            tasks.add(task);
        }

        return tasks;
    }


    public Task addTaskToNutzer(Long nutzerId, Task task) {
        Nutzer nutzer = nutzerRepository.findById(nutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        task = taskRepository.save(task);

        nutzer.getTaskIds().add(task.getId());
        nutzerRepository.save(nutzer);

        return task;
    }

    public void deleteTask(Long nutzerId, Long taskId) {
        // Holen Sie den Nutzer anhand seiner ID
        Nutzer nutzer = nutzerRepository.findById(nutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        // Überprüfen Sie, ob der Task dem Nutzer gehört
        if (!nutzer.getTaskIds().contains(taskId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Der Nutzer hat keinen Zugriff auf diesen Task");
        }

        // Überprüfen, ob der Task Teil eines Moduls ist
        if (taskRepository.existsByModuleId(taskId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Der Task ist Teil eines Moduls und kann nicht gelöscht werden");
        }

        // Löschen Sie den Task aus der Datenbank
        nutzer.getTaskIds().remove(taskId);
        taskRepository.deleteById(taskId);
    }


    //MODULE
    public List<Task> getTasksByModuleId(Long moduleId) {
        List<Long> taskIds = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"))
                .getTaskIds();

        return taskIds.stream()
                .map(taskId -> taskRepository.findById(taskId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task nicht gefunden")))
                .collect(Collectors.toList());
    }

    public Task addTaskToModule(Long moduleId, Task task) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));

        task = taskRepository.save(task);

        module.getTaskIds().add(task.getId());
        moduleRepository.save(module);

        return task;
    }

    //FILTER
    public List<Task> getTasksByStatus(Task.TaskStatus status) {
        List<Task> allTasks = taskRepository.findAll();
        return allTasks.stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }



}

