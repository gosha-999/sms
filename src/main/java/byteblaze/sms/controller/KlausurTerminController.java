package byteblaze.sms.controller;

import byteblaze.sms.model.KlausurTermin;
import byteblaze.sms.service.KlausurTerminService;
import byteblaze.sms.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/klausur")
public class KlausurTerminController {

    private final KlausurTerminService klausurTerminService;
    private final LoginService loginService;


    @PostMapping("/{moduleId}/add")
    public ResponseEntity<KlausurTermin> addKlausurTermin(@PathVariable Long moduleId, @RequestBody KlausurTermin klausurTermin) {
        KlausurTermin newKlausurTermin = klausurTerminService.addKlausurTermin(moduleId, klausurTermin);
        return ResponseEntity.status(HttpStatus.CREATED).body(newKlausurTermin);
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<List<KlausurTermin>> getKlausurTermineByModuleId(@PathVariable Long moduleId) {
        List<KlausurTermin> klausurTermine = klausurTerminService.getKlausurTermineByModuleId(moduleId);
        return ResponseEntity.ok(klausurTermine);
    }

    @PostMapping("/{klausurTerminId}/buchen")
    public ResponseEntity<?> bucheKlausurtermin(@PathVariable Long klausurTerminId, @RequestHeader("sessionId") String sessionId) {
        if (!loginService.isValidSession(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ungültige Sitzung");
        }
        Long userId = loginService.getUserIdFromSession(sessionId);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nutzer nicht identifiziert");
        }
        try {
            klausurTerminService.bucheKlausurtermin(userId, klausurTerminId);
            return ResponseEntity.ok("Klausurtermin erfolgreich gebucht");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/nutzerTermine")
    public ResponseEntity<?> getNutzerTermine(@RequestHeader("sessionId") String sessionId) {
        if (!loginService.isValidSession(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ungültige Sitzung");
        }
        Long nutzerId = loginService.getUserIdFromSession(sessionId);
        if (nutzerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nutzer nicht identifiziert");
        }
        try {
            List<KlausurTermin> nutzerTermine = klausurTerminService.getNutzerTermine(nutzerId);
            return ResponseEntity.ok(nutzerTermine);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
