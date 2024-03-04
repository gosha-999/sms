package byteblaze.sms.controller;

import byteblaze.sms.model.Task;
import byteblaze.sms.model.Task.TaskStatus;
import byteblaze.sms.service.LoginService;
import byteblaze.sms.service.NutzerService;
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

    // Endpunkt zum Hinzufügen eines Tasks zu einem Nutzer
    @PostMapping("/add")
    public ResponseEntity<Task> addTaskToNutzer(@RequestBody Task task) {
        Task addedTask = taskService.addTaskToNutzer(loginService.getLoggedInUserId(), task);
        return ResponseEntity.ok(addedTask);
    }

    // Endpunkt zum Abrufen der Task-IDs eines Nutzers
    @GetMapping("/{nutzerId}")
    public ResponseEntity<List<Long>> getTaskIdsByNutzerId(@PathVariable Long nutzerId) {
        List<Long> taskIds = taskService.getTaskIdsByNutzerId(nutzerId);
        return ResponseEntity.ok(taskIds);
    }

    //Nutzer löscht seinen Task
    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(loginService.getLoggedInUserId(), taskId);
        return ResponseEntity.ok("Task erfolgreich gelöscht");
    }

    // Endpunkt zum Abrufen der Task-IDs eines Moduls
    @GetMapping("/{moduleId}")
    public ResponseEntity<List<Long>> getTaskIdsByModuleId(@PathVariable Long moduleId) {
        List<Long> taskIds = taskService.getTaskIdsByModuleId(moduleId);
        return ResponseEntity.ok(taskIds);
    }

    // Endpunkt zum Hinzufügen eines Tasks zu einem Modul
    @PostMapping("/{moduleId}")
    public ResponseEntity<Task> addTaskToModule(@PathVariable Long moduleId, @RequestBody Task task) {
        Task addedTask = taskService.addTaskToModule(moduleId, task);
        return ResponseEntity.ok(addedTask);
    }
}

