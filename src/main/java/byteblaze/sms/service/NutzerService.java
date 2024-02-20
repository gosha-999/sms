package byteblaze.sms.service;

import byteblaze.sms.model.Module;
import byteblaze.sms.model.Nutzer;
import byteblaze.sms.repository.NutzerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
public class NutzerService {

    private final NutzerRepo nutzerRepo;

    @Autowired
    public NutzerService(NutzerRepo nutzerRepo) {
        this.nutzerRepo = nutzerRepo;
    }

    public Nutzer getNutzerInfo(Long moduleID) {
        return nutzerRepo.findById(moduleID).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));
    }

    public Nutzer addUser(Nutzer nutzer) {
        return nutzerRepo.save(nutzer); // Hier wird der Benutzer in der Datenbank gespeichert}
    }

    public Nutzer updateUser(Long nutzerID, Nutzer updatedNutzer) {
        Nutzer existingNutzer = nutzerRepo.findById(nutzerID).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden");
        });

        existingNutzer.setDetails(updatedNutzer.getDetails());
        existingNutzer.setNutzername(updatedNutzer.getNutzername());
        existingNutzer.setPassword(updatedNutzer.getPassword());

        return nutzerRepo.save(existingNutzer);
    }

    public void deleteUser(Long nutzerID) {
        nutzerRepo.deleteById(nutzerID);
    }

    public List<Nutzer> getAllNutzer() {
        return nutzerRepo.findAll();
    }

    public Set<Module> getMerklisteByNutzerId(Long nutzerId) {
        Nutzer nutzer = nutzerRepo.findById(nutzerId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));
        return nutzer.getMerkliste();
    }

    public Set<Module> getGebuchtById(Long nutzerId) {
        Nutzer nutzer = nutzerRepo.findById(nutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        return nutzer.getGebuchteModule();
    }
}
