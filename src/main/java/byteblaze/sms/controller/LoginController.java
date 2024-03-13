package byteblaze.sms.controller;

import byteblaze.sms.service.LoginService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String nutzername = loginRequest.getNutzername();
        String password = loginRequest.getPassword();
        if (loginService.authenticate(nutzername, password)) {
            Long userId = loginService.getUserIdByUsername(nutzername);
            String sessionId = loginService.createSession(userId);
            return ResponseEntity.ok(sessionId);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader String sessionId) {
        if (loginService.isValidSession(sessionId)) {
            loginService.logout(sessionId);
            return ResponseEntity.ok("Logout successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid session");
        }
    }
    @Data
    public static class LoginRequest {
        private String nutzername;
        private String password;
    }
}


