package byteblaze.sms.controller;

import byteblaze.sms.model.Task;
import byteblaze.sms.service.LoginService;
import byteblaze.sms.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final LoginService loginService;

    // Add a task to a user
    @PostMapping("/add")
    public ResponseEntity<?> addTaskToNutzer(@RequestBody Task task, @RequestHeader("sessionId") String sessionId) {
        if (!loginService.isValidSession(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ungültige Sitzung");
        }
        Long userId = loginService.getUserIdFromSession(sessionId);
        Task addedTask = taskService.addTaskToNutzer(userId, task);
        return ResponseEntity.ok(addedTask);
    }


    // Get task IDs by user ID
    @GetMapping("/get")
    public ResponseEntity<?> getTaskIdsByNutzerId(@RequestHeader("sessionId") String sessionId) {
        if (!loginService.isValidSession(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ungültige Sitzung");
        }
        Long userId = loginService.getUserIdFromSession(sessionId);
        List<Long> taskIds = taskService.getTaskIdsByNutzerId(userId);
        return ResponseEntity.ok(taskIds);
    }

    // Get all tasks by user ID
    @GetMapping("/tasks")
    public ResponseEntity<?> getTasksByNutzerId(@RequestHeader("sessionId") String sessionId) {
        if (!loginService.isValidSession(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ungültige Sitzung");
        }
        Long userId = loginService.getUserIdFromSession(sessionId);
        List<Task> tasks = taskService.getTasksByNutzerId(userId);
        return ResponseEntity.ok(tasks);
    }

    // User deletes a task by task ID
    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId, @RequestHeader("sessionId") String sessionId) {
        if (!loginService.isValidSession(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ungültige Sitzung");
        }
        Long userId = loginService.getUserIdFromSession(sessionId);
        taskService.deleteTask(userId, taskId);
        return ResponseEntity.ok("Task erfolgreich gelöscht");
    }

    // Get all tasks by module ID
    @GetMapping("/{moduleId}")
    public ResponseEntity<List<Task>> getTasksByModuleId(@PathVariable Long moduleId) {
        List<Task> tasks = taskService.getTasksByModuleId(moduleId);
        return ResponseEntity.ok(tasks);
    }

    // Adds a task to a module by module ID
    @PostMapping("/{moduleId}/add")
    public ResponseEntity<Task> addTaskToModule(@PathVariable Long moduleId, @RequestBody Task task) {
        Task addedTask = taskService.addTaskToModule(moduleId, task);
        return ResponseEntity.ok(addedTask);
    }

    // Endpoint for getting all tasks with a specific status
    @GetMapping("/filter")
    public ResponseEntity<List<Task>> getTasksByStatus(@RequestParam Task.TaskStatus status) {
        List<Task> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    //Status updaten
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<?> updateTaskStatus(@PathVariable Long taskId, @RequestBody String status, @RequestHeader("sessionId") String sessionId) {
        if (!loginService.isValidSession(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ungültige Sitzung");
        }
        // Konvertiere den String status in ein Enum
        Task.TaskStatus newStatus;
        try {
            newStatus = Task.TaskStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ungültiger Status");
        }
        Task updatedTask = taskService.updateTaskStatus(taskId, newStatus);
        return ResponseEntity.ok(updatedTask);
    }

}
