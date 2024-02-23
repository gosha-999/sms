package byteblaze.sms.service;

import byteblaze.sms.model.Module;
import byteblaze.sms.model.Nutzer;
import byteblaze.sms.repository.ModuleRepo;
import byteblaze.sms.repository.NutzerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final NutzerRepo nutzerRepo;
    private final ModuleRepo moduleRepo;

    @Autowired
    public EnrollmentService(NutzerRepo nutzerRepo, ModuleRepo moduleRepo) {
        this.nutzerRepo = nutzerRepo;
        this.moduleRepo = moduleRepo;
    }


    public void addToGebucht(Long nutzerId, Long moduleId) {
        Nutzer nutzer = nutzerRepo.findById(nutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        Module module = moduleRepo.findById(moduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));

        int totalECTS = nutzer.getGebuchteModule().stream().mapToInt(Module::getEcts).sum();
        int moduleECTS = module.getEcts();
        int totalWithModule = totalECTS + moduleECTS;

        if (totalWithModule <= 30) {
            nutzer.getGebuchteModule().add(module);
            nutzerRepo.save(nutzer);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Die Gesamtanzahl der ECTS-Punkte überschreitet 30");
        }
    }




    public void removeFromGebucht(Long nutzerId, Long moduleId) {
        Nutzer nutzer = nutzerRepo.findById(nutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        Module module = moduleRepo.findById(moduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));

        nutzer.getMerkliste().remove(module);
        nutzerRepo.save(nutzer);
    }

    public Set<Module> getEnrolledModules(Long nutzerId) {
        Nutzer nutzer = nutzerRepo.findById(nutzerId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        return nutzer.getGebuchteModule();
    }

    // Service-Methode, um mehrere Noten für Module einzutragen
    public void addNotesForModules(Long nutzerId, Map<Long, Double> moduleNotes) {
        Nutzer nutzer = nutzerRepo.findById(nutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        for (Map.Entry<Long, Double> entry : moduleNotes.entrySet()) {
            Long moduleId = entry.getKey();
            Double note = entry.getValue();

            // Überprüfen, ob das Modul bereits gebucht wurde
            if (!nutzer.getGebuchteModule().stream().anyMatch(module -> module.getModuleId() == moduleId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Das Modul wurde nicht gebucht");
            }

            // Validierung der Note
            if (note < 1.0 || note > 5.0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Die Note muss zwischen 1,0 und 5,0 liegen");
            }

            nutzer.getNoten().put(moduleId, note);
        }

        nutzerRepo.save(nutzer);
    }

    public Map<Long, Double> getAllNotes(Long nutzerId) {
        Nutzer nutzer = nutzerRepo.findById(nutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        return nutzer.getNoten();
    }

    //FILTER BENOTET JA/NEIN
    public List<Module> getBookedModules(Long nutzerId, boolean benotet) {
        Nutzer nutzer = nutzerRepo.findById(nutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        return nutzer.getGebuchteModule().stream()
                .filter(module -> {
                    boolean hasNote = nutzer.getNoten().containsKey(module.getModuleId());
                    return (benotet && hasNote) || (!benotet && !hasNote);
                })
                .collect(Collectors.toList());
    }




}

