package byteblaze.sms.service;

import byteblaze.sms.model.KlausurTermin;
import byteblaze.sms.model.Nutzer;
import byteblaze.sms.repository.KlausurTerminRepository;
import byteblaze.sms.repository.ModuleRepository;
import byteblaze.sms.repository.NutzerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KlausurTerminService {

    private final KlausurTerminRepository klausurTerminRepository;
    private final ModuleRepository moduleRepository;
    private final NutzerRepository nutzerRepository;

    public KlausurTermin addKlausurTermin(Long moduleId, KlausurTermin klausurTermin) {
        // Überprüfen, ob das Modul existiert
        if (!moduleRepository.existsById(moduleId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden");
        }

        // Setzen der Modul-ID für den Klausurtermin
        klausurTermin.setModuleId(moduleId);
        klausurTermin.setVerbleibendePlätze(klausurTermin.getMaxPlätze()); // Setzen der verbleibenden Plätze

        // Speichern des Klausurtermins
        klausurTerminRepository.save(klausurTermin);
        return klausurTermin;
    }

    public List<KlausurTermin> getKlausurTermineByModuleId(Long moduleId) {
        // Überprüfen, ob das Modul existiert
        if (!moduleRepository.existsById(moduleId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden");
        }

        // Rückgabe der Klausurtermine für das angegebene Modul
        return klausurTerminRepository.findByModuleId(moduleId);
    }

    public void bucheKlausurtermin(Long nutzerId, Long klausurTerminId) {
        Nutzer nutzer = nutzerRepository.findById(nutzerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        KlausurTermin klausurTermin = klausurTerminRepository.findById(klausurTerminId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Klausurtermin nicht gefunden"));

        // Überprüfen, ob noch Plätze verfügbar sind
        if (klausurTermin.getVerbleibendePlätze() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Es sind keine Plätze mehr verfügbar");
        }

        // Hole das Modul-ID des Klausurtermins
        Long moduleId = klausurTermin.getModuleId();

        // Überprüfe, ob der Nutzer bereits für einen Klausurtermin dieses Moduls gebucht hat
        // Dies nutzt die Methode findByModuleId(Long moduleId) im KlausurTerminRepository
        List<KlausurTermin> gebuchteKlausurTermineFuerModul = klausurTerminRepository.findByModuleId(moduleId);
        boolean hatBereitsGebucht = gebuchteKlausurTermineFuerModul.stream()
                .anyMatch(gebuchterTermin -> nutzer.getGebuchteKlausurTerminIds().contains(gebuchterTermin.getKlausurTerminId()));

        if (hatBereitsGebucht) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Der Nutzer hat bereits für einen Klausurtermin dieses Moduls gebucht");
        }

        // Füge den Nutzer zum Klausurtermin hinzu
        nutzer.getGebuchteKlausurTerminIds().add(klausurTerminId);
        nutzerRepository.save(nutzer);

        // Füge den Nutzer zur Liste der gebuchten Nutzer hinzu
        klausurTermin.getGebuchtVonNutzerIds().add(nutzerId);
        klausurTermin.setVerbleibendePlätze(klausurTermin.getMaxPlätze() - klausurTermin.getGebuchtVonNutzerIds().size());
        klausurTerminRepository.save(klausurTermin);

        // Aktualisieren der verbleibenden Plätze
        int remainingSeats = klausurTermin.getMaxPlätze() - klausurTermin.getGebuchtVonNutzerIds().size();
        if (remainingSeats <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Keine verbleibenden Plätze für diesen Klausurtermin");
        }
        klausurTermin.setVerbleibendePlätze(remainingSeats);
        klausurTerminRepository.save(klausurTermin);
    }

    public List<KlausurTermin> getNutzerTermine(Long nutzerId) {
        // Suche alle Klausurtermine, die von diesem Nutzer gebucht wurden
        return klausurTerminRepository.findAll().stream()
                .filter(klausurTermin -> klausurTermin.getGebuchtVonNutzerIds().contains(nutzerId))
                .collect(Collectors.toList());
    }

}
