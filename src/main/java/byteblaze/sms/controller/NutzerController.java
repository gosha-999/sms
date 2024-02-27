package byteblaze.sms.controller;

import byteblaze.sms.model.Module;
import byteblaze.sms.model.Nutzer;
import byteblaze.sms.service.EnrollmentService;
import byteblaze.sms.service.LoginService;
import byteblaze.sms.service.MerklisteService;
import byteblaze.sms.service.NutzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/nutzer")
public class NutzerController {

    private final NutzerService nutzerService;
    private final MerklisteService merklisteService;
    private final EnrollmentService enrollmentService;
    private final LoginService loginService;

    @Autowired
    public NutzerController(NutzerService nutzerService, MerklisteService merklisteService, EnrollmentService enrollmentService, LoginService loginService){
        this.nutzerService = nutzerService;
        this.merklisteService = merklisteService;
        this.enrollmentService = enrollmentService;
        this.loginService = loginService;
    }

    @GetMapping("/{nutzerID}")
    public ResponseEntity<Nutzer> getNutzerById(@PathVariable Long nutzerID) {
        Nutzer nutzer = nutzerService.getNutzerInfo(nutzerID);
            return ResponseEntity.ok(nutzer);
    }

    @PostMapping
    public ResponseEntity<Nutzer> addUser(@RequestBody Nutzer nutzer) {
        Nutzer newNutzer = nutzerService.addUser(nutzer);
        return ResponseEntity.created(null).body(newNutzer);
    }

    @PutMapping("/update")
    public ResponseEntity<Nutzer> updateUser(@RequestBody Nutzer updatedNutzer) {
        Nutzer updatedUser = nutzerService.updateUser(loginService.getLoggedInUserId(), updatedNutzer);
            return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser() {
        nutzerService.deleteUser(loginService.getLoggedInUserId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Nutzer>> getAllNutzer() {
        List<Nutzer> nutzerList = nutzerService.getAllNutzer();
        return ResponseEntity.ok(nutzerList);
    }

    @PostMapping("/merkliste/module/{moduleId}")
    public ResponseEntity<String> addToMerkliste(@PathVariable Long moduleId) {
        try {
            merklisteService.addToMerkliste(loginService.getLoggedInUserId(), moduleId);
            return ResponseEntity.ok("Modul wurde zur Merkliste hinzugefügt");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fehler beim Hinzufügen zum Merkliste");
        }
    }

    @DeleteMapping("/merkliste/module/{moduleId}")
    public ResponseEntity<String> removeFromMerkliste(@PathVariable Long moduleId) {
        try {
            merklisteService.removeFromMerkliste(loginService.getLoggedInUserId(),moduleId);
            return ResponseEntity.ok("Modul wurde aus der Merkliste entfernt");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fehler beim Entfernen aus der Merkliste");
        }
    }

    //GIBT DIE MERKLISTE EINES NUTZERS AUS
    @GetMapping("/{nutzerId}/merkliste")
    public ResponseEntity<Set<Module>> getMerklisteByNutzerId(@PathVariable Long nutzerId) {
        Set<Module> merkliste = merklisteService.getMerklisteByNutzerId(nutzerId);
        return ResponseEntity.ok(merkliste);
    }


    //BUCHE MODUL
    @PostMapping("/gebucht/{moduleID}")
    public ResponseEntity<String> addToGebucht(@PathVariable Long moduleID) {
            enrollmentService.addToGebucht(loginService.getLoggedInUserId(), moduleID);
            return ResponseEntity.ok("Modul wurde zu gebucht");
    }

    //MODUL AUS BUCHUNG LÖSCHEN
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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Nutzer nutzer) {
        Long nutzerId = loginService.login(nutzer.getNutzername(), nutzer.getPassword());
        if (nutzerId != null) {
            return ResponseEntity.ok("Login erfolgreich. Nutzer-ID: " + nutzerId);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ungültige Anmeldeinformationen.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        loginService.logout();
        return ResponseEntity.ok("Logout erfolgreich.");
    }
}