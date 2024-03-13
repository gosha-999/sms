package byteblaze.sms.service;

import byteblaze.sms.model.Module;
import byteblaze.sms.model.Task;
import byteblaze.sms.repository.ModuleRepository;
import byteblaze.sms.repository.NutzerRepository;
import byteblaze.sms.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final NutzerRepository nutzerRepository;
    private final TaskRepository taskRepository;



    public Module getModuleInfo(Long moduleId) {
        return moduleRepository.findById(moduleId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));
    }

    public Module addModule(Module module){
        return moduleRepository.save(module);
    }

    public Module updateModule(Long moduleId, Module updatedModule) {
        Module existingModule = moduleRepository.findById(moduleId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));

        existingModule.setName(updatedModule.getName());
        existingModule.setBeschreibung(updatedModule.getBeschreibung());
        existingModule.setEcts(updatedModule.getEcts());
        existingModule.setDozent(updatedModule.getDozent());
        existingModule.setLehrstuhl(updatedModule.getLehrstuhl());
        existingModule.setRegeltermin(updatedModule.getRegeltermin());
        existingModule.setLiteraturempfehlung(updatedModule.getLiteraturempfehlung());

        return moduleRepository.save(existingModule);
    }



    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    public void deleteModule(Long moduleId) {
        // Überprüfen, ob das Modul von einem Nutzer gebucht wurde
        boolean moduleBooked = nutzerRepository.existsByGebuchteModuleModuleId(moduleId);
        if (moduleBooked) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Das Modul kann nicht gelöscht werden, da es von einem Nutzer gebucht ist");
        }

        // Löschen des Moduls, wenn kein Nutzer es gebucht hat
        moduleRepository.deleteById(moduleId);
    }

}

