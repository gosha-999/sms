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

        Long klausurModulId = klausurTermin.getModuleId();

        boolean isModuleBooked = nutzer.getGebuchteModule().stream()
                .anyMatch(module -> module.getModuleId() ==klausurModulId);

        if (!isModuleBooked) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Das Modul ist nicht in den gebuchten Modulen des Nutzers enthalten");
        }

        boolean hasKlausurTermin = nutzer.getGebuchteKlausurTermine().stream()
                .anyMatch(termin -> termin.getModuleId() ==klausurModulId);

        if (hasKlausurTermin) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Der Nutzer hat bereits einen Klausurtermin für dieses Modul gebucht");
        }

        // Überprüfen, ob der Klausurtermin bereits ausgebucht ist
        if (klausurTermin.getNutzerIds().size() >= klausurTermin.getMaxPlätze()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Der Klausurtermin ist bereits ausgebucht");
        }

        // Überprüfen, ob der Nutzer bereits für diesen Klausurtermin gebucht hat
        if (klausurTermin.getNutzerIds().contains(nutzerId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Der Nutzer hat bereits einen Klausurtermin für dieses Modul gebucht");
        }

        // Hinzufügen des Nutzers zur Liste der Nutzer-IDs des Klausurtermins
        klausurTermin.getNutzerIds().add(nutzerId);
        nutzer.getGebuchteKlausurTermine().add(klausurTermin);

        // Aktualisieren der verbleibenden Plätze
        int remainingSeats = klausurTermin.getMaxPlätze() - klausurTermin.getNutzerIds().size();
        if (remainingSeats <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Keine verbleibenden Plätze für diesen Klausurtermin");
        }
        klausurTermin.setVerbleibendePlätze(remainingSeats);
        klausurTerminRepository.save(klausurTermin);
    }

}
