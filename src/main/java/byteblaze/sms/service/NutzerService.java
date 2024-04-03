package byteblaze.sms.service;

import byteblaze.sms.model.Nutzer;
import byteblaze.sms.repository.NutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class NutzerService {

    private final NutzerRepository nutzerRepository;

    @Autowired
    public NutzerService(NutzerRepository nutzerRepository) {
        this.nutzerRepository = nutzerRepository;
    }

    public Nutzer getNutzerInfo(Long nutzerId) {
        return nutzerRepository.findById(nutzerId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));
    }

    public Nutzer addUser(Nutzer nutzer) {
        String nutzername = nutzer.getNutzername();

        // Überprüfen, ob der Benutzername bereits existiert (ohne Groß- und Kleinschreibung zu berücksichtigen)
        if (existsByUsername(nutzer.getNutzername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Benutzername bereits vergeben");
        }

        // Wenn der Benutzername eindeutig ist, füge hinzu
        return nutzerRepository.save(nutzer);
    }

    public Nutzer updateUser(Long nutzerId, Nutzer updatedNutzer) {
        Nutzer existingNutzer = nutzerRepository.findById(nutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        if (updatedNutzer.getEmail() != null) {
            existingNutzer.setEmail(updatedNutzer.getEmail());
        }
        if (updatedNutzer.getNutzername() != null) {
            existingNutzer.setNutzername(updatedNutzer.getNutzername());
        }
        if (updatedNutzer.getPassword() != null) {
            existingNutzer.setPassword(updatedNutzer.getPassword());
        }
        if (updatedNutzer.getSemester() != 0) {
            existingNutzer.setSemester(updatedNutzer.getSemester());
        }

        return nutzerRepository.save(existingNutzer);
    }


    public void deleteUser(Long nutzerId) {
        nutzerRepository.deleteById(nutzerId);
    }

    public List<Nutzer> getAllNutzer() {
        return nutzerRepository.findAll();
    }

    public boolean existsByUsername(String nutzername) {
        return nutzerRepository.findByNutzername(nutzername) != null;
    }

}
