package byteblaze.sms.controller;

import byteblaze.sms.model.Module;
import byteblaze.sms.service.LoginService;
import byteblaze.sms.service.MerklisteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class MerklisteController {


    private final MerklisteService merklisteService;
    private final LoginService loginService;

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
            merklisteService.removeFromMerkliste(loginService.getLoggedInUserId(), moduleId);
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
}
