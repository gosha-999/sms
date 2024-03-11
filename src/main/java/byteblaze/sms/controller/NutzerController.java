package byteblaze.sms.controller;

import byteblaze.sms.model.Module;
import byteblaze.sms.model.Nutzer;
import byteblaze.sms.service.EnrollmentService;
import byteblaze.sms.service.LoginService;
import byteblaze.sms.service.MerklisteService;
import byteblaze.sms.service.NutzerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/nutzer")
public class NutzerController {

    private final NutzerService nutzerService;
    private final LoginService loginService;

    //GET Nutzer per ID
    @GetMapping("/{nutzerID}")
    public ResponseEntity<Nutzer> getNutzerById(@PathVariable Long nutzerID) {
        Nutzer nutzer = nutzerService.getNutzerInfo(nutzerID);
            return ResponseEntity.ok(nutzer);
    }

    //ADD Nutzer (Registrierung)
    @PostMapping("/add")
    public ResponseEntity<Nutzer> addUser(@RequestBody Nutzer nutzer) {
        Nutzer newNutzer = nutzerService.addUser(nutzer);
        return ResponseEntity.created(null).body(newNutzer);
    }

    //UPDATE Nutzer (nur der eingeloggte Nutzer kann seine Daten ändern)
    @PutMapping("/update")
    public ResponseEntity<Nutzer> updateUser(@RequestBody Nutzer updatedNutzer) {
        Nutzer updatedUser = nutzerService.updateUser(loginService.getLoggedInUserId(), updatedNutzer);
            return ResponseEntity.ok(updatedUser);
    }

    //Löschen eines Users (nur der eingeloggte kann sich selbst löschen)
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser() {
        nutzerService.deleteUser(loginService.getLoggedInUserId());
        return ResponseEntity.noContent().build();
    }

    //GET alle Nutzer
    @GetMapping("/all")
    public ResponseEntity<List<Nutzer>> getAllNutzer() {
        List<Nutzer> nutzerList = nutzerService.getAllNutzer();
        return ResponseEntity.ok(nutzerList);
    }







}