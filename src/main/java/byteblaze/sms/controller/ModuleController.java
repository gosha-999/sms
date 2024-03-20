package byteblaze.sms.controller;

import byteblaze.sms.model.Module;
import byteblaze.sms.service.LoginService;
import byteblaze.sms.service.ModuleService;
import byteblaze.sms.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/modules")
public class ModuleController {

    private final ModuleService moduleService;
    private final RatingService ratingService;
    private final LoginService loginService;


    //GET Modul per moduleId
    @GetMapping("/{moduleId}")
    public ResponseEntity<Module> getModuleById(@PathVariable Long moduleId) {
        Module module = moduleService.getModuleInfo(moduleId);
        return ResponseEntity.ok(module);
    }

    //erstellt ein neues Modul
    @PostMapping("/add")
    public ResponseEntity<Module> addModule(@RequestBody Module module) {
        Module newModule = moduleService.addModule(module);
        return ResponseEntity.created(null).body(newModule);
    }

    //updated ein bestehendes Modul per moduleId
    @PutMapping("/{moduleId}/update")
    public ResponseEntity<Module> updateModule(@PathVariable Long moduleId, @RequestBody Module updatedModule) {
        Module module = moduleService.updateModule(moduleId, updatedModule);
        return ResponseEntity.ok(module);
    }

    //löscht ein Modul per moduleId
    @DeleteMapping("/{moduleId}/delete")
    public ResponseEntity<Void> deleteModule(@PathVariable Long moduleId) {
        moduleService.deleteModule(moduleId);
        return ResponseEntity.noContent().build();
    }

    //gibt alle existierenden AddModule zurück (Modulübersicht)
    @GetMapping("/all")
    public ResponseEntity<List<Module>> getAllModules() {
        List<Module> modules = moduleService.getAllModules();
        return ResponseEntity.ok(modules);
    }

    //bewertet ein Modul per moduleId
    // im Body wird das Rating übergeben (1-5)
    @PostMapping("/{moduleId}/rating")
    public ResponseEntity<String> bewerteModul(@PathVariable Long moduleId, @RequestHeader("sessionId") String sessionId, @RequestBody int rating) {
        if (!loginService.isValidSession(sessionId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ungültige Sitzung");
        }

        Long userId = loginService.getUserIdFromSession(sessionId);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nicht authentifiziert");
        }

        ratingService.addModuleRating(moduleId, userId, rating);
        return ResponseEntity.status(HttpStatus.CREATED).body("Bewertung erfolgreich hinzugefügt");
    }


}
