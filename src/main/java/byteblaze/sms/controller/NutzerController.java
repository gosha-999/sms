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

    @GetMapping("/{nutzerId}/merkliste")
    public ResponseEntity<Set<Module>> getMerklisteByNutzerId(@PathVariable Long nutzerId) {
        Set<Module> merkliste = nutzerService.getMerklisteByNutzerId(nutzerId);
        return ResponseEntity.ok(merkliste);
    }


    @PostMapping("/{nutzerID}/gebucht/{moduleID}")
    public ResponseEntity<String> addToGebucht(@PathVariable Long nutzerID, @PathVariable Long moduleID) {
            enrollmentService.addToGebucht(nutzerID, moduleID);
            return ResponseEntity.ok("Modul wurde zu gebucht");
    }

    @DeleteMapping("/{nutzerID}/gebucht/{moduleID}")
    public ResponseEntity<String> removeFromGebucht(@PathVariable Long nutzerID, @PathVariable Long moduleID) {
            enrollmentService.removeFromGebucht(nutzerID, moduleID);
            return ResponseEntity.ok("Gebuchtes Modul wurde erfolgreich gelöscht");
    }

    @GetMapping("/{nutzerId}/gebucht")
    public ResponseEntity<Set<Module>> getGebuchtById(@PathVariable Long nutzerId) {
        Set<Module> gebuchteModule = nutzerService.getGebuchtById(nutzerId);
        return ResponseEntity.ok(gebuchteModule);
    }

}