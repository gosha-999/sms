package byteblaze.sms.service;

import byteblaze.sms.model.Nutzer;
import byteblaze.sms.model.Module;
import byteblaze.sms.repository.ModuleRepo;
import byteblaze.sms.repository.NutzerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MerklisteService {

    private final NutzerRepo nutzerRepo;
    private final ModuleRepo moduleRepo;

    @Autowired
    public MerklisteService(NutzerRepo nutzerRepo, ModuleRepo moduleRepo) {
        this.nutzerRepo = nutzerRepo;
        this.moduleRepo = moduleRepo;
    }

    public void addToMerkliste(Long nutzerID, Long moduleID) {
        Nutzer nutzer = nutzerRepo.findById(nutzerID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        Module module = moduleRepo.findById(moduleID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));

        nutzer.getMerkliste().add(module);
        nutzerRepo.save(nutzer);
    }

    public void removeFromMerkliste(Long nutzerID, Long moduleID) {
        Nutzer nutzer = nutzerRepo.findById(nutzerID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        Module module = moduleRepo.findById(moduleID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));

        nutzer.getMerkliste().remove(module);
        nutzerRepo.save(nutzer);
    }
}
