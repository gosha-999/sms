package byteblaze.sms.controller;

import byteblaze.sms.model.Module;
import byteblaze.sms.service.ModuleService;
import byteblaze.sms.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/modules")
public class ModuleController {

    private final ModuleService moduleService;
    private final RatingService ratingService;

    @Autowired
    public ModuleController(ModuleService moduleService, RatingService ratingService) {

        this.moduleService = moduleService;
        this.ratingService = ratingService;
    }

    @GetMapping("/{moduleID}")
    public ResponseEntity<Module> getModuleById(@PathVariable Long moduleID) {
        Module module = moduleService.getModuleInfo(moduleID);
        return ResponseEntity.ok(module);
    }

    @PostMapping
    public ResponseEntity<Module> addModule(@RequestBody Module module) {
        Module newModule = moduleService.addModule(module);
        return ResponseEntity.created(null).body(newModule);
    }

    @PutMapping("/{moduleID}")
    public ResponseEntity<Module> updateModule(@PathVariable Long moduleID, @RequestBody Module updatedModule) {
        Module module = moduleService.updateModule(moduleID, updatedModule);
        return ResponseEntity.ok(module);
    }

    @DeleteMapping("/{moduleID}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long moduleID) {
        moduleService.deleteModule(moduleID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Module>> getAllModules() {
        List<Module> modules = moduleService.getAllModules();
        return ResponseEntity.ok(modules);
    }

    //Bewerten
    @PostMapping("/{moduleID}/rating/{nutzerID}")
    public ResponseEntity<String> addModuleRating(@PathVariable Long moduleID, @PathVariable Long nutzerID, @RequestBody int rating) {
        ratingService.addModuleRating(moduleID, nutzerID, rating);
        return ResponseEntity.status(HttpStatus.CREATED).body("Bewertung wurde erfolgreich hinzugefügt");
    }
}
