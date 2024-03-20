package byteblaze.sms.service;

import byteblaze.sms.model.Module;
import byteblaze.sms.model.Nutzer;
import byteblaze.sms.repository.ModuleRepository;
import byteblaze.sms.repository.NutzerRepository;
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

    private final NutzerRepository nutzerRepository;
    private final ModuleRepository moduleRepository;

    @Autowired
    public EnrollmentService(NutzerRepository nutzerRepository, ModuleRepository moduleRepository) {
        this.nutzerRepository = nutzerRepository;
        this.moduleRepository = moduleRepository;
    }


    public void addToGebucht(Long nutzerId, Long moduleId) {
        Nutzer nutzer = nutzerRepository.findById(nutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));

        // Überprüfen Sie, ob das Semester des Nutzers dem Semester des Moduls entspricht oder höher ist
        if (nutzer.getSemester() < module.getMinSemester()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Der Nutzer darf das Modul nicht buchen, da sein Semester niedriger ist");
        }

        if (nutzer.getGebuchteModule().contains(module)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Der Nutzer hat das Modul bereits gebucht");
        }

        int totalECTSWithoutGrades = nutzer.getGebuchteModule().stream()
                .filter(m -> !nutzer.getNoten().containsKey(m.getModuleId()))
                .mapToInt(Module::getEcts)
                .sum();

        if (totalECTSWithoutGrades + module.getEcts() > 30) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Die maximale Anzahl von 30 ECTS ist bereits erreicht");
        }

        nutzer.getGebuchteModule().add(module);

        // Überprüfen Sie, ob die Modul-ID in der Merkliste des Nutzers vorhanden ist
        if (nutzer.getMerkliste().contains(module)) {
            nutzer.getMerkliste().remove(module); // Entfernen Sie das Modul aus der Merkliste des Nutzers
        }

        // Fügen Sie die Task-IDs des Moduls zur Liste der Task-IDs des Nutzers hinzu
        nutzer.getTaskIds().addAll(module.getTaskIds());

        nutzerRepository.save(nutzer);
    }

    public void removeFromGebucht(Long nutzerId, Long moduleId) {
        Nutzer nutzer = nutzerRepository.findById(nutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));

        nutzer.getGebuchteModule().remove(module);
        nutzerRepository.save(nutzer);
    }

    public Set<Module> getEnrolledModules(Long nutzerId) {
        Nutzer nutzer = nutzerRepository.findById(nutzerId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        return nutzer.getGebuchteModule();
    }

    // Service-Methode, um mehrere Noten für Module einzutragen
    public void addNotesForModules(Long nutzerId, Map<Long, Double> moduleNotes) {
        Nutzer nutzer = nutzerRepository.findById(nutzerId)
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

        nutzerRepository.save(nutzer);
    }

    public Map<Long, Double> getAllNotes(Long nutzerId) {
        Nutzer nutzer = nutzerRepository.findById(nutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        return nutzer.getNoten();
    }

    //FILTER BENOTET JA/NEIN
    public List<Module> getBookedModules(Long nutzerId, boolean benotet) {
        Nutzer nutzer = nutzerRepository.findById(nutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        return nutzer.getGebuchteModule().stream()
                .filter(module -> {
                    boolean hasNote = nutzer.getNoten().containsKey(module.getModuleId());
                    return (benotet && hasNote) || (!benotet && !hasNote);
                })
                .collect(Collectors.toList());
    }

    public double calculateWeightedAverageGrade(Long nutzerId) {
        Nutzer nutzer = nutzerRepository.findById(nutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        Map<Long, Double> noten = nutzer.getNoten();

        if (noten.isEmpty()) {
            return 0.0; // Wenn der Nutzer keine Noten hat, beträgt der Durchschnitt 0
        }

        double sumWeightedGrade = 0.0;
        int sumECTS = 0;

        for (Map.Entry<Long, Double> entry : noten.entrySet()) {
            Long moduleId = entry.getKey();
            Double note = entry.getValue();

            Module module = moduleRepository.findById(moduleId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));

            int ects = module.getEcts();
            sumWeightedGrade += note * ects;
            sumECTS += ects;
        }

        return sumWeightedGrade / sumECTS;
    }




}

