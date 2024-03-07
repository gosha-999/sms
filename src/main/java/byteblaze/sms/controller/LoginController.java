package byteblaze.sms.controller;

import byteblaze.sms.model.Nutzer;
import byteblaze.sms.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    //LOGIN Nutzer - manche Methoden benutzen nur noch die ID des eingeloggten Users
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Nutzer nutzer) {
        Long nutzerId = loginService.login(nutzer.getNutzername(), nutzer.getPassword());
        if (nutzerId != null) {
            return ResponseEntity.ok("Login erfolgreich. Nutzer-ID: " + nutzerId);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ungültige Anmeldeinformationen.");
        }
    }

    //LOGOUT Nutzer
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        loginService.logout();
        return ResponseEntity.ok("Logout erfolgreich.");
    }
}
