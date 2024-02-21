package byteblaze.sms.service;

import byteblaze.sms.model.Module;
import byteblaze.sms.model.Nutzer;
import byteblaze.sms.repository.ModuleRepo;
import byteblaze.sms.repository.NutzerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RatingService {

    private final ModuleRepo moduleRepo;
    private final NutzerRepo nutzerRepo;

    @Autowired
    public RatingService(ModuleRepo moduleRepo, NutzerRepo nutzerRepo) {
        this.moduleRepo = moduleRepo;
        this.nutzerRepo = nutzerRepo;
    }

    public void addModuleRating(Long moduleID, Long nutzerID, int rating) {
        // Überprüfen, ob der Nutzer eine Note für das Modul gesetzt hat
        Nutzer nutzer = nutzerRepo.findById(nutzerID).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Nutzer nicht gefunden"));

        Module module = moduleRepo.findById(moduleID).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul nicht gefunden"));

        if (!nutzer.getNoten().containsKey(moduleID)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Der Nutzer hat keine Note für dieses Modul gesetzt");
        }

        // Überprüfen, ob der Nutzer bereits eine Bewertung für das Modul abgegeben hat
        //fehlt

        // Bewertung speichern
        nutzer.getNoten().put(moduleID, (double) rating);
        nutzerRepo.save(nutzer);

        // Durchschnittliche Bewertung des Moduls aktualisieren
        double currentAverageRating = module.getDurchschnittlicheBewertung();
        int currentNumberOfRatings = module.getAnzahlBewertungen();

        double newAverageRating = ((currentAverageRating * currentNumberOfRatings) + rating) / (currentNumberOfRatings + 1);
        module.setDurchschnittlicheBewertung(newAverageRating);
        module.setAnzahlBewertungen(currentNumberOfRatings + 1);

        moduleRepo.save(module);
    }


}
