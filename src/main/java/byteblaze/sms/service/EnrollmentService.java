package byteblaze.sms.service;

import byteblaze.sms.model.Module;
import byteblaze.sms.model.Nutzer;
import byteblaze.sms.repository.ModuleRepo;
import byteblaze.sms.repository.NutzerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EnrollmentService {

    private final NutzerRepo nutzerRepo;
    private final ModuleRepo moduleRepo;

    @Autowired
    public EnrollmentService(NutzerRepo nutzerRepo, ModuleRepo moduleRepo) {
        this.nutzerRepo = nutzerRepo;
        this.moduleRepo = moduleRepo;
    }


    public void addToGebucht(Long nutzerID, Long moduleID) {
        Nutzer nutzer = nutzerRepo.findById(nutzerID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        Module module = moduleRepo.findById(moduleID)
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




    public void removeFromGebucht(Long nutzerID, Long moduleID) {
        Nutzer nutzer = nutzerRepo.findById(nutzerID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        Module module = moduleRepo.findById(moduleID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));

        nutzer.getMerkliste().remove(module);
        nutzerRepo.save(nutzer);
    }




}

