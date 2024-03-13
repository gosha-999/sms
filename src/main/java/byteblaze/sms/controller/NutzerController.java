package byteblaze.sms.controller;

import byteblaze.sms.model.Nutzer;
import byteblaze.sms.service.NutzerService;
import byteblaze.sms.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nutzer")
public class NutzerController {

    private final NutzerService nutzerService;
    private final LoginService loginService;

    public NutzerController(NutzerService nutzerService, LoginService loginService) {
        this.nutzerService = nutzerService;
        this.loginService = loginService;
    }

    @GetMapping("/{nutzerID}")
    public ResponseEntity<Nutzer> getNutzerById(@PathVariable Long nutzerID) {
        Nutzer nutzer = nutzerService.getNutzerInfo(nutzerID);
        return ResponseEntity.ok(nutzer);
    }

    @PostMapping("/add")
    public ResponseEntity<Nutzer> addUser(@RequestBody Nutzer nutzer) {
        Nutzer newNutzer = nutzerService.addUser(nutzer);
        return ResponseEntity.status(HttpStatus.CREATED).body(newNutzer);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestHeader("sessionId") String sessionId, @RequestBody Nutzer updatedNutzer) {
        if (!loginService.isValidSession(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }
        Long userId = loginService.getUserIdFromSession(sessionId);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User ID not found in session");
        }
        Nutzer updatedUser = nutzerService.updateUser(userId, updatedNutzer);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestHeader("sessionId") String sessionId) {
        if (!loginService.isValidSession(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }
        Long userId = loginService.getUserIdFromSession(sessionId);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User ID not found in session");
        }
        nutzerService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Nutzer>> getAllNutzer() {
        List<Nutzer> nutzerList = nutzerService.getAllNutzer();
        return ResponseEntity.ok(nutzerList);
    }
}
