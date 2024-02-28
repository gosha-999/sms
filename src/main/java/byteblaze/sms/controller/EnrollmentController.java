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
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final LoginService loginService;


    //BUCHE MODUL PER MODULID
    @PostMapping("/gebucht/{moduleID}")
    public ResponseEntity<String> addToGebucht(@PathVariable Long moduleID) {
        enrollmentService.addToGebucht(loginService.getLoggedInUserId(), moduleID);
        return ResponseEntity.ok("Modul wurde zu gebucht");
    }

    //ENTFERNE MODUL AUS BUCHUNSLISTE
    @DeleteMapping("/gebucht/{moduleID}")
    public ResponseEntity<String> removeFromGebucht(@PathVariable Long moduleID) {
        enrollmentService.removeFromGebucht(loginService.getLoggedInUserId(), moduleID);
        return ResponseEntity.ok("Gebuchtes Modul wurde erfolgreich gelöscht");
    }

    //GIBT ALLE GEBUCHTEN MODULE EINES NUTZERS ZURÜCK
    @GetMapping("/{nutzerId}/gebucht")
    public ResponseEntity<Set<Module>> getEnrolledModules(@PathVariable Long nutzerId) {
        Set<Module> enrolledModules = enrollmentService.getEnrolledModules(nutzerId);
        return ResponseEntity.ok(enrolledModules);
    }

    //FILTERN DER GEBUCHT LISTE NACH BENOTET TRUE FALSE
    @GetMapping("/gebuchtfilter")
    public List<Module> getBookedModulesForUser(@RequestParam(required = false) boolean benotet) {
        return enrollmentService.getBookedModules(loginService.getLoggedInUserId(), benotet);
    }

    //MODUL BENOTEN
    @PostMapping("/noten")
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

    //SCHNITT ALLER NOTEN
    @GetMapping("/durchschnittsnote")
    public ResponseEntity<Double> getWeightedAverageGrade() {
        double averageGrade = enrollmentService.calculateWeightedAverageGrade(loginService.getLoggedInUserId());
        return ResponseEntity.ok(averageGrade);
    }
}
