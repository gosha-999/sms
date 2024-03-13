package byteblaze.sms.controller;

import byteblaze.sms.model.Module;
import byteblaze.sms.service.EnrollmentService;
import byteblaze.sms.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("enrollment")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final LoginService loginService;

    @PostMapping("/{moduleID}/add")
    public ResponseEntity<String> addToGebucht(@PathVariable Long moduleID, @RequestHeader("sessionId") String sessionId) {
        if(!loginService.isValidSession(sessionId)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ungültige Sitzung");
        }
        Long userId = loginService.getUserIdFromSession(sessionId);
        if (userId != null){
            enrollmentService.addToGebucht(userId, moduleID);
            return ResponseEntity.ok("Modul erfolgreich gebucht");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    @DeleteMapping("/{moduleID}/delete")
    public ResponseEntity<String> removeFromGebucht(@PathVariable Long moduleID, @RequestHeader("sessionId") String sessionId) {
        if (!loginService.isValidSession(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid session");
        }
        Long userId = loginService.getUserIdFromSession(sessionId);
        if (userId != null) {
            enrollmentService.removeFromGebucht(userId, moduleID);
            return ResponseEntity.ok("Gebuchtes Modul wurde erfolgreich gelöscht");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Set<Module>> getEnrolledModules(@RequestHeader("sessionId") String sessionId) {
        if (!loginService.isValidSession(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = loginService.getUserIdFromSession(sessionId);
        Set<Module> enrolledModules = enrollmentService.getEnrolledModules(userId);
        return ResponseEntity.ok(enrolledModules);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Module>> getBookedModulesForUser(@RequestHeader("sessionId") String sessionId, @RequestParam(required = false) boolean benotet) {
        if (!loginService.isValidSession(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = loginService.getUserIdFromSession(sessionId);
        List<Module> modules = enrollmentService.getBookedModules(userId, benotet);
        return ResponseEntity.ok(modules);
    }

    @PostMapping("/setnoten")
    public ResponseEntity<Void> addNotesForModules(@RequestHeader("sessionId") String sessionId, @RequestBody Map<Long, Double> moduleNotes) {
        if (!loginService.isValidSession(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = loginService.getUserIdFromSession(sessionId);
        enrollmentService.addNotesForModules(userId, moduleNotes);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/noten")
    public ResponseEntity<Map<Long, Double>> getAllNotes(@RequestHeader("sessionId") String sessionId) {
        if (!loginService.isValidSession(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = loginService.getUserIdFromSession(sessionId);
        Map<Long, Double> allNotes = enrollmentService.getAllNotes(userId);
        return ResponseEntity.ok(allNotes);
    }

    @GetMapping("/durchschnitt")
    public ResponseEntity<Double> getWeightedAverageGrade(@RequestHeader("sessionId") String sessionId) {
        if (!loginService.isValidSession(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = loginService.getUserIdFromSession(sessionId);
        double averageGrade = enrollmentService.calculateWeightedAverageGrade(userId);
        return ResponseEntity.ok(averageGrade);
    }
}
