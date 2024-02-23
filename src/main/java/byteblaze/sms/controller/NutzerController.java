package byteblaze.sms.controller;

import byteblaze.sms.model.Module;
import byteblaze.sms.model.Nutzer;
import byteblaze.sms.service.EnrollmentService;
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

    @Autowired
    public NutzerController(NutzerService nutzerService, MerklisteService merklisteService, EnrollmentService enrollmentService){
        this.nutzerService = nutzerService;
        this.merklisteService = merklisteService;
        this.enrollmentService = enrollmentService;
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

    @PutMapping("/{nutzerID}")
    public ResponseEntity<Nutzer> updateUser(@PathVariable Long nutzerID, @RequestBody Nutzer updatedNutzer) {
        Nutzer updatedUser = nutzerService.updateUser(nutzerID, updatedNutzer);
            return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{nutzerID}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long nutzerID) {
        nutzerService.deleteUser(nutzerID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Nutzer>> getAllNutzer() {
        List<Nutzer> nutzerList = nutzerService.getAllNutzer();
        return ResponseEntity.ok(nutzerList);
    }

    @PostMapping("/{nutzerId}/merkliste/module/{moduleId}")
    public ResponseEntity<String> addToMerkliste(@PathVariable Long nutzerId, @PathVariable Long moduleId) {
        try {
            merklisteService.addToMerkliste(nutzerId, moduleId);
            return ResponseEntity.ok("Modul wurde zur Merkliste hinzugefügt");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fehler beim Hinzufügen zum Merkliste");
        }
    }

    @DeleteMapping("/{nutzerId}/merkliste/module/{moduleId}")
    public ResponseEntity<String> removeFromMerkliste(@PathVariable Long nutzerId, @PathVariable Long moduleId) {
        try {
            merklisteService.removeFromMerkliste(nutzerId, moduleId);
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
    @PostMapping("/{nutzerID}/gebucht/{moduleID}")
    public ResponseEntity<String> addToGebucht(@PathVariable Long nutzerID, @PathVariable Long moduleID) {
            enrollmentService.addToGebucht(nutzerID, moduleID);
            return ResponseEntity.ok("Modul wurde zu gebucht");
    }

    //MODUL AUS BUCHUNG LÖSCHEN
    @DeleteMapping("/{nutzerID}/gebucht/{moduleID}")
    public ResponseEntity<String> removeFromGebucht(@PathVariable Long nutzerID, @PathVariable Long moduleID) {
            enrollmentService.removeFromGebucht(nutzerID, moduleID);
            return ResponseEntity.ok("Gebuchtes Modul wurde erfolgreich gelöscht");
    }

    //GIBT ALLE GEBUCHTEN MODULE EINES NUTZERS ZURÜCK
    @GetMapping("/{nutzerId}/gebucht")
    public ResponseEntity<Set<Module>> getEnrolledModules(@PathVariable Long nutzerId) {
        Set<Module> enrolledModules = enrollmentService.getEnrolledModules(nutzerId);
        return ResponseEntity.ok(enrolledModules);
    }

    //FILTERN DER GEBUCHT LISTE NACH BENOTET TRUE FALSE
    @GetMapping("/{nutzerId}/gebuchtfilter")
    public List<Module> getBookedModulesForUser(@PathVariable Long nutzerId, @RequestParam(required = false) boolean benotet) {
        return enrollmentService.getBookedModules(nutzerId, benotet);
    }

    //MODUL BENOTEN
    @PostMapping("/{nutzerId}/noten")
    public ResponseEntity<Void> addNotesForModules(@PathVariable Long nutzerId, @RequestBody Map<Long, Double> moduleNotes) {
        enrollmentService.addNotesForModules(nutzerId, moduleNotes);
        return ResponseEntity.ok().build();
    }

    //GIBT ALLE NOTEN EINES NUTZERS ZURÜCK
    @GetMapping("/{nutzerId}/noten")
    public ResponseEntity<Map<Long, Double>> getAllNotes(@PathVariable Long nutzerId) {
        Map<Long, Double> allNotes = enrollmentService.getAllNotes(nutzerId);
        return ResponseEntity.ok(allNotes);
    }

    //login
    @PostMapping("/login")
    public ResponseEntity<Nutzer> login(@RequestBody Map<String, String> loginRequest) {
        String nutzername = loginRequest.get("nutzername");
        String password = loginRequest.get("password");
        Nutzer nutzer = nutzerService.login(nutzername, password);
        return ResponseEntity.ok(nutzer);
    }

    //login 2
    @PostMapping("/loginpa")
    public ResponseEntity<Nutzer> loginpa(@RequestParam String nutzername, @RequestParam String password) {
        Nutzer angemeldeterNutzer = nutzerService.login(nutzername, password);
        return ResponseEntity.ok(angemeldeterNutzer);
    }





}