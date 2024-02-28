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


    @GetMapping("/{moduleId}")
    public ResponseEntity<Module> getModuleById(@PathVariable Long moduleId) {
        Module module = moduleService.getModuleInfo(moduleId);
        return ResponseEntity.ok(module);
    }

    @PostMapping
    public ResponseEntity<Module> addModule(@RequestBody Module module) {
        Module newModule = moduleService.addModule(module);
        return ResponseEntity.created(null).body(newModule);
    }

    @PutMapping("/{moduleId}")
    public ResponseEntity<Module> updateModule(@PathVariable Long moduleId, @RequestBody Module updatedModule) {
        Module module = moduleService.updateModule(moduleId, updatedModule);
        return ResponseEntity.ok(module);
    }

    @DeleteMapping("/{moduleId}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long moduleId) {
        moduleService.deleteModule(moduleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Module>> getAllModules() {
        List<Module> modules = moduleService.getAllModules();
        return ResponseEntity.ok(modules);
    }

    //BEWERTEN EINES MODULES PER MODULEID
    @PostMapping("/{moduleId}/rating")
    public ResponseEntity<String> bewerteModul(@PathVariable Long moduleId, @RequestBody int rating) {
        ratingService.addModuleRating(moduleId, loginService.getLoggedInUserId(), rating);
        return ResponseEntity.status(HttpStatus.CREATED).body("Bewertung erfolgreich hinzugefügt");
    }

}
