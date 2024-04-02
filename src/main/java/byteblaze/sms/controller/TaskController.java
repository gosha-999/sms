package byteblaze.sms.controller;

import byteblaze.sms.model.Task;
import byteblaze.sms.service.LoginService;
import byteblaze.sms.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final LoginService loginService;


    // Endpoint zum Hinzufügen eines individuellen Tasks
    @PostMapping("/add")
    public ResponseEntity<Task> addIndividualTask(@RequestBody Task task, @RequestHeader("sessionId") String sessionId) {
        Long nutzerId = loginService.getUserIdFromSession(sessionId);
        if (nutzerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Task newTask = taskService.addIndividualTask(nutzerId, task);
        return ResponseEntity.ok(newTask);
    }

    // Endpoint zum Abrufen aller Tasks eines Nutzers
    @GetMapping("/get")
    public ResponseEntity<List<Task>> getAllTasksForNutzer(@RequestHeader("sessionId") String sessionId) {
        Long nutzerId = loginService.getUserIdFromSession(sessionId);
        if (nutzerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Task> tasks = taskService.getAllTasksForNutzer(nutzerId);
        return ResponseEntity.ok(tasks);
    }

    // Endpoint zum Hinzufügen eines Modul-Tasks
    @PostMapping("/module/{moduleId}")
    public ResponseEntity<Task> addModuleTask(@PathVariable Long moduleId, @RequestBody Task task) {
        Task newTask = taskService.addModuleTask(moduleId, task);
        return ResponseEntity.ok(newTask);
    }

    // Endpoint zum Abrufen aller Tasks eines Moduls
    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<Task>> getTasksByModuleId(@PathVariable Long moduleId) {
        List<Task> tasks = taskService.getTasksByModuleId(moduleId);
        return ResponseEntity.ok(tasks);
    }

    // Endpunkt zum Aktualisieren eines Nutzer-Tasks
    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateNutzerTask(@PathVariable Long taskId, @RequestBody Task task, @RequestHeader("sessionId") String sessionId) {
        Long nutzerId = loginService.getUserIdFromSession(sessionId);
        if (nutzerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Task updatedTask = taskService.updateNutzerTask(nutzerId, taskId, task);
        return ResponseEntity.ok(updatedTask);
    }

    // Endpunkt zum Löschen eines Nutzer-Tasks
    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteNutzerTask(@PathVariable Long taskId, @RequestHeader("sessionId") String sessionId) {
        Long nutzerId = loginService.getUserIdFromSession(sessionId);
        if (nutzerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        taskService.deleteNutzerTask(nutzerId, taskId);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{taskId}/status")
    public ResponseEntity<Task> updateTaskStatus(@RequestHeader("sessionId") String sessionId,@PathVariable Long taskId, @RequestBody Task updatedTask) {
        Long nutzerId = loginService.getUserIdFromSession(sessionId);
        if (nutzerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // Aktualisieren des Task-Status im Service
        Task updatedTaskResult = taskService.updateTaskStatus(nutzerId, taskId, updatedTask);
        // Rückgabe des aktualisierten Tasks
        return ResponseEntity.ok(updatedTaskResult);
    }

}