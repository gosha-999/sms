package byteblaze.sms.service;

import byteblaze.sms.model.Nutzer;
import byteblaze.sms.repository.NutzerRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class LoginService {

    private Long loggedInUserId;
    private final NutzerRepository nutzerRepository;

    @Autowired
    public LoginService(NutzerRepository nutzerRepository) {
        this.nutzerRepository = nutzerRepository;
    }

    public Long login(String nutzername, String password) {
        Nutzer nutzer = nutzerRepository.findByNutzernameAndPassword(nutzername, password);
        if (nutzer != null) {
            loggedInUserId = nutzer.getNutzerId();
            return loggedInUserId;
        } else {
            return null;
        }
    }

    public void logout() {
        loggedInUserId = null;
    }
}

