package byteblaze.sms.controller;

import byteblaze.sms.model.Task;
import byteblaze.sms.model.Task.TaskStatus;
import byteblaze.sms.service.LoginService;
import byteblaze.sms.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final LoginService loginService;



    @PostMapping("/{moduleId}")
    public ResponseEntity<Task> createTaskAndAssignToModule(@PathVariable Long moduleId, @RequestBody Task task) {
        Task createdTask = taskService.addTaskToModule(moduleId, task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }




}

