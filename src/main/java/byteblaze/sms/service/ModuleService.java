package byteblaze.sms.service;

import byteblaze.sms.model.Module;
import byteblaze.sms.repository.ModuleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ModuleService {

    private final ModuleRepo moduleRepo;

    @Autowired
    public ModuleService(ModuleRepo moduleRepo){
        this.moduleRepo = moduleRepo;
    }

    public Module getModuleInfo(Long moduleID) {
        return moduleRepo.findById(moduleID).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));
    }

    public Module addModule(Module module){
        return moduleRepo.save(module);
    }

    public Module updateModule(Long moduleID, Module updatedModule) {
        Module existingModule = moduleRepo.findById(moduleID).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));

        existingModule.setBeschreibung(updatedModule.getBeschreibung());
        existingModule.setEcts(updatedModule.getEcts());
        existingModule.setDozent(updatedModule.getDozent());
        existingModule.setLehrstuhl(updatedModule.getLehrstuhl());
        existingModule.setRegeltermin(updatedModule.getRegeltermin());
        existingModule.setLiteraturempfehlung(updatedModule.getLiteraturempfehlung());

        return moduleRepo.save(existingModule);
    }

    public void deleteModule(Long moduleID) {
        moduleRepo.deleteById(moduleID);
    }

    public List<Module> getAllModules() {
        return moduleRepo.findAll();
    }
}

