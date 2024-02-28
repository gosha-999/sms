package byteblaze.sms.service;

import byteblaze.sms.model.Module;
import byteblaze.sms.model.Nutzer;
import byteblaze.sms.repository.ModuleRepository;
import byteblaze.sms.repository.NutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class RatingService {

    private final ModuleRepository moduleRepository;
    private final NutzerRepository nutzerRepository;

    @Autowired
    public RatingService(ModuleRepository moduleRepository, NutzerRepository nutzerRepository) {
        this.moduleRepository = moduleRepository;
        this.nutzerRepository = nutzerRepository;
    }

    public void addModuleRating(Long moduleID, Long nutzerID, int rating) {
        Nutzer nutzer = nutzerRepository.findById(nutzerID).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        Module module = moduleRepository.findById(moduleID).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));

        // Überprüfen, ob der Nutzer bereits eine Bewertung für das Modul abgegeben hat
        Map<Long, Integer> bewertungen = module.getBewertungen();
        if (bewertungen.containsKey(nutzerID)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Der Nutzer hat bereits eine Bewertung für dieses Modul abgegeben");
        }

        // Sicherstellen, dass der Nutzer eine Note für das Modul gesetzt hat
        Map<Long, Double> noten = nutzer.getNoten();
        if (!noten.containsKey(moduleID)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Der Nutzer hat keine Note für dieses Modul gesetzt");
        }

        // Sicherstellen, dass die Bewertung im Bereich von 1 bis 5 liegt
        if (rating < 1 || rating > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Die Bewertung muss im Bereich von 1 bis 5 liegen");
        }

        // Bewertung hinzufügen
        bewertungen.put(nutzerID, rating);
        module.setBewertungen(bewertungen);

        // Durchschnittliche Bewertung des Moduls aktualisieren
        double currentAverageRating = calculateAverageRating(module.getBewertungen());
        module.setDurchschnittlicheBewertung(currentAverageRating);

        // Anzahl der Bewertungen aktualisieren
        int numberOfRatings = bewertungen.size();
        module.setAnzahlBewertungen(numberOfRatings);

        // Modul speichern
        moduleRepository.save(module);
    }

    private double calculateAverageRating(Map<Long, Integer> bewertungen) {
        int sum = 0;
        for (int rating : bewertungen.values()) {
            sum += rating;
        }
        return (double) sum / bewertungen.size();
    }


}
