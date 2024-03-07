package byteblaze.sms.controller;

import byteblaze.sms.model.Module;
import byteblaze.sms.service.EnrollmentService;
import byteblaze.sms.service.LoginService;
import lombok.RequiredArgsConstructor;
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


    //Buche ein Modul per moduleId für eingeloggten Nutzer
    @PostMapping("/{moduleID}/add")
    public ResponseEntity<String> addToGebucht(@PathVariable Long moduleID) {
        enrollmentService.addToGebucht(loginService.getLoggedInUserId(), moduleID);
        return ResponseEntity.ok("Modul wurde zu gebucht");
    }

    //entfernt das Modul per moduleId des eingeloggten Nutzers
    @DeleteMapping("/{moduleID}/delete")
    public ResponseEntity<String> removeFromGebucht(@PathVariable Long moduleID) {
        enrollmentService.removeFromGebucht(loginService.getLoggedInUserId(), moduleID);
        return ResponseEntity.ok("Gebuchtes Modul wurde erfolgreich gelöscht");
    }

    //gibt alle gebuchten module eines nutzers zurück
    @GetMapping("/get")
    public ResponseEntity<Set<Module>> getEnrolledModules() {
        Set<Module> enrolledModules = enrollmentService.getEnrolledModules(loginService.getLoggedInUserId());
        return ResponseEntity.ok(enrolledModules);
    }

    //filtern der Liste nach benotet(true) oder unbenotet(false)
    @GetMapping("/filter")
    public List<Module> getBookedModulesForUser(@RequestParam(required = false) boolean benotet) {
        return enrollmentService.getBookedModules(loginService.getLoggedInUserId(), benotet);
    }

    //mehrere module werden per map im body benotet bspw. moduleId{"1": 1,0}Note
    @PostMapping("/setnoten")
    public ResponseEntity<Void> addNotesForModules(@RequestBody Map<Long, Double> moduleNotes) {
        enrollmentService.addNotesForModules(loginService.getLoggedInUserId(), moduleNotes);
        return ResponseEntity.ok().build();
    }

    //GIBT ALLE NOTEN EINES NUTZERS ZURÜCK
    @GetMapping("/noten")
    public ResponseEntity<Map<Long, Double>> getAllNotes() {
        Map<Long, Double> allNotes = enrollmentService.getAllNotes(loginService.getLoggedInUserId());
        return ResponseEntity.ok(allNotes);
    }

    //durchschnitt aller noten unter berücksichtigung aller ects
    @GetMapping("/durchschnitt")
    public ResponseEntity<Double> getWeightedAverageGrade() {
        double averageGrade = enrollmentService.calculateWeightedAverageGrade(loginService.getLoggedInUserId());
        return ResponseEntity.ok(averageGrade);
    }
}
