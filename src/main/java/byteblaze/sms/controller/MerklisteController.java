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

    // Fügt das Modul der Merkliste des Nutzers per modulId hinzu
    @PostMapping("/{moduleId}/add")
    public ResponseEntity<String> addToMerkliste(@PathVariable Long moduleId, @RequestHeader("sessionId") String sessionId) {
        if (!loginService.isValidSession(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ungültige Sitzung");
        }
        Long userId = loginService.getUserIdFromSession(sessionId);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nutzer nicht identifiziert");
        }
        try {
            merklisteService.addToMerkliste(userId, moduleId);
            return ResponseEntity.ok("Modul wurde zur Merkliste hinzugefügt");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fehler beim Hinzufügen zur Merkliste");
        }
    }

    // Löscht das Modul aus der Merkliste des Nutzers per modulId
    @DeleteMapping("/{moduleId}/delete")
    public ResponseEntity<String> removeFromMerkliste(@PathVariable Long moduleId, @RequestHeader("sessionId") String sessionId) {
        if (!loginService.isValidSession(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ungültige Sitzung");
        }
        Long userId = loginService.getUserIdFromSession(sessionId);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nutzer nicht identifiziert");
        }
        try {
            merklisteService.removeFromMerkliste(userId, moduleId);
            return ResponseEntity.ok("Modul wurde aus der Merkliste entfernt");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fehler beim Entfernen aus der Merkliste");
        }
    }

    // Gibt die Merkliste eines Nutzers per nutzerId aus
    @GetMapping("")
    public ResponseEntity<Set<Module>> getMerklisteByNutzerId(@RequestHeader("sessionId") String sessionId) {
        if (!loginService.isValidSession(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Set.of());
        }
        Long userId = loginService.getUserIdFromSession(sessionId);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Set<Module> merkliste = merklisteService.getMerklisteByNutzerId(userId);
        return ResponseEntity.ok(merkliste);
    }
}
