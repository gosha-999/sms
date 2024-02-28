package byteblaze.sms.service;

import byteblaze.sms.model.Nutzer;
import byteblaze.sms.model.Module;
import byteblaze.sms.repository.ModuleRepository;
import byteblaze.sms.repository.NutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class MerklisteService {

    private final NutzerRepository nutzerRepository;
    private final ModuleRepository moduleRepository;

    @Autowired
    public MerklisteService(NutzerRepository nutzerRepository, ModuleRepository moduleRepository) {
        this.nutzerRepository = nutzerRepository;
        this.moduleRepository = moduleRepository;
    }

    //ADD
    public void addToMerkliste(Long nutzerID, Long moduleId) {
        Nutzer nutzer = nutzerRepository.findById(nutzerID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));

        nutzer.getMerkliste().add(module);
        nutzerRepository.save(nutzer);
    }

    //DELETE
    public void removeFromMerkliste(Long nutzerId, Long moduleId) {
        Nutzer nutzer = nutzerRepository.findById(nutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));

        nutzer.getMerkliste().remove(module);
        nutzerRepository.save(nutzer);
    }

    public Set<Module> getMerklisteByNutzerId(Long nutzerId) {
        Nutzer nutzer = nutzerRepository.findById(nutzerId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));
        return nutzer.getMerkliste();
    }
}
