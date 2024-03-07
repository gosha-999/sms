package byteblaze.sms.controller;

import byteblaze.sms.model.Module;
import byteblaze.sms.service.LoginService;
import byteblaze.sms.service.MerklisteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/merkliste")
public class MerklisteController {


    private final MerklisteService merklisteService;
    private final LoginService loginService;

    //fügt das Modul der Merkliste des Nutzers per modulId hinzu
    @PostMapping("/{moduleId}/add")
    public ResponseEntity<String> addToMerkliste(@PathVariable Long moduleId) {
        try {
            merklisteService.addToMerkliste(loginService.getLoggedInUserId(), moduleId);
            return ResponseEntity.ok("Modul wurde zur Merkliste hinzugefügt");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fehler beim Hinzufügen zum Merkliste");
        }
    }

    //löscht das Modul aus der Merkliste des Nutzers per modulId
    @DeleteMapping("/{moduleId}/delete")
    public ResponseEntity<String> removeFromMerkliste(@PathVariable Long moduleId) {
        try {
            merklisteService.removeFromMerkliste(loginService.getLoggedInUserId(), moduleId);
            return ResponseEntity.ok("Modul wurde aus der Merkliste entfernt");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fehler beim Entfernen aus der Merkliste");
        }
    }

    //gibt die Merkliste eines Nutzers per nutzerId aus
    @GetMapping("")
    public ResponseEntity<Set<Module>> getMerklisteByNutzerId() {
        Set<Module> merkliste = merklisteService.getMerklisteByNutzerId(loginService.getLoggedInUserId());
        return ResponseEntity.ok(merkliste);
    }
}
