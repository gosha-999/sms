package byteblaze.sms.service;

import byteblaze.sms.model.Module;
import byteblaze.sms.model.Nutzer;
import byteblaze.sms.model.Task;
import byteblaze.sms.repository.ModuleRepository;
import byteblaze.sms.repository.NutzerRepository;
import byteblaze.sms.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ModuleRepository moduleRepository;
    private final NutzerRepository nutzerRepository;


    // Fügt einen individuellen Task für einen Nutzer hinzu
    public Task addIndividualTask(Long nutzerId, Task task) {
        task.setNutzerId(nutzerId);
        // Setze moduleId auf null, um es als individuellen Task zu markieren
        task.setModuleId(null);
        return taskRepository.save(task);
    }

    // Erstellt Kopien der Modul-Tasks für einen Nutzer, wenn dieser ein Modul bucht
    public void assignModuleTasksToUser(Long moduleId, Long nutzerId) {
        List<Task> moduleTasks = taskRepository.findByModuleId(moduleId);
        List<Task> copiedTasks = moduleTasks.stream().map(task -> {
            Task copy = new Task();
            copy.setTitle(task.getTitle());
            copy.setDescription(task.getDescription());
            copy.setDeadline(task.getDeadline());
            copy.setStatus(task.getStatus());
            copy.setModuleId(task.getModuleId());
            copy.setNutzerId(nutzerId);
            return taskRepository.save(copy);
        }).collect(Collectors.toList());
    }

    // Holt alle Tasks für einen Nutzer, inklusive individuelle und Modul-Tasks
    public List<Task> getAllTasksForNutzer(Long nutzerId) {
        return taskRepository.findByNutzerId(nutzerId);
    }

    // Methode zum Hinzufügen eines Modul-Tasks
    public Task addModuleTask(Long moduleId, Task task) {
        task.setModuleId(moduleId);
        // Stelle sicher, dass der Task keinem spezifischen Nutzer zugeordnet ist, wenn er erstellt wird
        task.setNutzerId(null);
        return taskRepository.save(task);
    }

    // Methode zum Abrufen aller Tasks, die zu einem bestimmten Modul gehören
    public List<Task> getTasksByModuleId(Long moduleId) {
        return taskRepository.findByModuleId(moduleId);
    }

    // Methode zum Entfernen aller ModulTasks eines Nutzers basierend auf der moduleId
    @Transactional
    public void removeTasksByModuleIdForNutzer(Long moduleId, Long nutzerId) {
        // Finde alle Tasks, die zum gegebenen Modul und Nutzer gehören
        List<Task> tasksToRemove = taskRepository.findByModuleIdAndNutzerId(moduleId, nutzerId);

        // Lösche die gefundenen Tasks
        for (Task task : tasksToRemove) {
            taskRepository.delete(task);
        }
    }

    // Methode zum Aktualisieren eines Nutzer-Tasks
    public Task updateNutzerTask(Long nutzerId, Long taskId, Task updatedTaskDetails) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task nicht gefunden"));

        if (!task.getNutzerId().equals(nutzerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Nicht berechtigt, diesen Task zu bearbeiten");
        }

        task.setTitle(updatedTaskDetails.getTitle());
        task.setDescription(updatedTaskDetails.getDescription());
        task.setDeadline(updatedTaskDetails.getDeadline());

        return taskRepository.save(task);
    }

    // Methode zum Löschen eines Nutzer-Tasks
    public void deleteNutzerTask(Long nutzerId, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task nicht gefunden"));

        if (!task.getNutzerId().equals(nutzerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Nicht berechtigt, diesen Task zu löschen");
        }

        taskRepository.delete(task);
    }

    public Task updateTaskStatus(Long nutzerId, Long taskId, Task updatedTask) {
        // Überprüfen, ob der Task dem Nutzer gehört
        Task existingTask = taskRepository.findByIdAndNutzerId(taskId, nutzerId);
        if (existingTask == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found or does not belong to the user");
        }

        // Überprüfen, ob updatedTask nicht null ist, bevor der Status aktualisiert wird
        if (updatedTask == null || updatedTask.getStatus() == null) {
            throw new IllegalArgumentException("Updated task or status cannot be null");
        }

        // Aktualisiere den Status des Tasks
        existingTask.setStatus(updatedTask.getStatus());

        // Speichern und Rückgabe des aktualisierten Tasks
        return taskRepository.save(existingTask);
    }

}

