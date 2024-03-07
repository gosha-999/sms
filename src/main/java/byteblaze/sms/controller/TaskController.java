package byteblaze.sms.controller;

import byteblaze.sms.model.Task;
import byteblaze.sms.service.LoginService;
import byteblaze.sms.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final LoginService loginService;

    //füge ein Task einem Nutzer zu
    @PostMapping("/add")
    public ResponseEntity<Task> addTaskToNutzer(@RequestBody Task task) {
        Task addedTask = taskService.addTaskToNutzer(loginService.getLoggedInUserId(), task);
        return ResponseEntity.ok(addedTask);
    }

    //UNNÖTIG
    //rufe alle TaskIDs eines Nutzers auf
    @GetMapping("/get")
    public ResponseEntity<List<Long>> getTaskIdsByNutzerId() {
        List<Long> taskIds = taskService.getTaskIdsByNutzerId(loginService.getLoggedInUserId());
        return ResponseEntity.ok(taskIds);
    }

    //Ruft alle Tasks eines Nutzers auf
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasksByNutzerId() {
        Long nutzerId = loginService.getLoggedInUserId();
        List<Task> tasks = taskService.getTasksByNutzerId(nutzerId);
        return ResponseEntity.ok(tasks);
    }

    //Nutzer löscht einen Task per taskId
    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(loginService.getLoggedInUserId(), taskId);
        return ResponseEntity.ok("Task erfolgreich gelöscht");
    }

    //abrufen aller TaskIds eines Moduls
    @GetMapping("/{moduleId}")
    public ResponseEntity<List<Task>> getTasksByModuleId(@PathVariable Long moduleId) {
        List<Task> tasks = taskService.getTasksByModuleId(moduleId);
        return ResponseEntity.ok(tasks);
    }

    //fügt einem Modul per moduleId einen Task hinzu
    @PostMapping("/{moduleId}/add")
    public ResponseEntity<Task> addTaskToModule(@PathVariable Long moduleId, @RequestBody Task task) {
        Task addedTask = taskService.addTaskToModule(moduleId, task);
        return ResponseEntity.ok(addedTask);
    }

    // Endpunkt zum Abrufen aller Tasks mit einem bestimmten Status
    @GetMapping("/filter")
    public ResponseEntity<List<Task>> getTasksByStatus(@RequestParam Task.TaskStatus status) {
        List<Task> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }
}

