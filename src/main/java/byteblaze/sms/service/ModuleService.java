package byteblaze.sms.service;

import byteblaze.sms.model.Module;
import byteblaze.sms.repository.ModuleRepo;
import byteblaze.sms.repository.NutzerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ModuleService {

    private final ModuleRepo moduleRepo;
    private final NutzerRepo nutzerRepo;

    @Autowired
    public ModuleService(ModuleRepo moduleRepo, NutzerRepo nutzerRepo){
        this.moduleRepo = moduleRepo;
        this.nutzerRepo = nutzerRepo;
    }

    public Module getModuleInfo(Long moduleId) {
        return moduleRepo.findById(moduleId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));
    }

    public Module addModule(Module module){
        return moduleRepo.save(module);
    }

    public Module updateModule(Long moduleId, Module updatedModule) {
        Module existingModule = moduleRepo.findById(moduleId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));

        existingModule.setBeschreibung(updatedModule.getBeschreibung());
        existingModule.setEcts(updatedModule.getEcts());
        existingModule.setDozent(updatedModule.getDozent());
        existingModule.setLehrstuhl(updatedModule.getLehrstuhl());
        existingModule.setRegeltermin(updatedModule.getRegeltermin());
        existingModule.setLiteraturempfehlung(updatedModule.getLiteraturempfehlung());

        return moduleRepo.save(existingModule);
    }



    public List<Module> getAllModules() {
        return moduleRepo.findAll();
    }

    public void deleteModule(Long moduleId) {
        // Überprüfen, ob das Modul von einem Nutzer gebucht wurde
        boolean moduleBooked = nutzerRepo.existsByGebuchteModuleModuleId(moduleId);
        if (moduleBooked) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Das Modul kann nicht gelöscht werden, da es von einem Nutzer gebucht ist");
        }

        // Löschen des Moduls, wenn kein Nutzer es gebucht hat
        moduleRepo.deleteById(moduleId);
    }

}

