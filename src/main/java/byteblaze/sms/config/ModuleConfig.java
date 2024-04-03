package byteblaze.sms.config;

import byteblaze.sms.model.KlausurTermin;
import byteblaze.sms.model.Module;
import byteblaze.sms.model.Task;
import byteblaze.sms.repository.KlausurTerminRepository;
import byteblaze.sms.repository.ModuleRepository;
import byteblaze.sms.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class ModuleConfig {
    @Bean
    CommandLineRunner commandLineRunner(ModuleRepository moduleRepository, TaskRepository taskRepository, KlausurTerminRepository klausurTerminRepository){
        return args -> {
            // Beispiel für das erste Modul
            Module module1 = new Module();
            module1.setName("Programmierung Grundlagen");
            module1.setBeschreibung("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, quis gravida magna mi a libero. Fusce vulputate eleifend sapien. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, metus. Nullam accumsan lorem in dui. Cras ultricies mi eu turpis hendrerit fringilla. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; In ac dui quis mi consectetuer lacinia. Nam pretium turpis et arcu. Duis arcu tortor, suscipit eget, imperdiet nec, imperdiet iaculis, ipsum. Sed aliquam ultrices mauris. Integer ante arcu, accumsan a, consectetuer eget, posuere ut, mauris. Praesent adipiscing. Phasellus ullamcorper ipsum rutrum nunc. Nunc nonummy metus. Vestibulum volutpat pretium libero. Cras id dui. Aenean ut");
            module1.setEcts(6);
            module1.setDozent("Dr. Müller");
            module1.setLehrstuhl("Informatik");
            module1.setRegeltermin("Montag 10:00 Uhr");
            module1.setLiteraturempfehlung("Programmierung für Anfänger");
            module1.setMinSemester(1);
            module1.setDurchschnittlicheBewertung(4.5);
            module1.setAnzahlBewertungen(20);
            moduleRepository.save(module1);


            // Klausurtermin für Modul 1
            KlausurTermin klausur1 = new KlausurTermin();
            klausur1.setDatum(LocalDate.of(2024, 6, 20));
            klausur1.setKlausurName("Abschlussklausur Programmierung Grundlagen");
            klausur1.setMaxPlätze(100);
            klausur1.setVerbleibendePlätze(100);
            klausur1.setModuleId(module1.getModuleId());
            klausurTerminRepository.save(klausur1);

            // Beispiel für das zweite Modul: Datenbankmanagement
            Module module2 = new Module();
            module2.setName("Datenbankmanagement");
            module2.setBeschreibung("Einführung in Datenbankmanagementsysteme");
            module2.setEcts(5);
            module2.setDozent("Prof. Schmidt");
            module2.setLehrstuhl("Informationssysteme");
            module2.setRegeltermin("Mittwoch 14:00 Uhr");
            module2.setLiteraturempfehlung("Datenbankgrundlagen");
            module2.setMinSemester(2);
            module2.setDurchschnittlicheBewertung(4.2);
            module2.setAnzahlBewertungen(18);
            module2 = moduleRepository.save(module2);

            // Klausurtermin für Modul 2
            KlausurTermin klausur2 = new KlausurTermin();
            klausur2.setDatum(LocalDate.of(2024, 7, 10));
            klausur2.setKlausurName("Datenbankmanagement Klausur");
            klausur2.setMaxPlätze(100);
            klausur2.setVerbleibendePlätze(100);
            klausur2.setModuleId(module2.getModuleId());
            klausurTerminRepository.save(klausur2);

            Module module3 = new Module();
            module3.setName("Objektorientierte Programmierung");
            module3.setBeschreibung("Vertiefung in objektorientierte Programmierung");
            module3.setEcts(7);
            module3.setDozent("Prof. Fischer");
            module3.setLehrstuhl("Softwaretechnik");
            module3.setRegeltermin("Dienstag 13:00 Uhr");
            module3.setLiteraturempfehlung("OOP-Konzepte");
            module3.setMinSemester(3);
            module3.setDurchschnittlicheBewertung(4.7);
            module3.setAnzahlBewertungen(22);

            Module module4 = new Module();
            module4.setName("Algorithmen und Datenstrukturen");
            module4.setBeschreibung("Grundlagen von Algorithmen und Datenstrukturen");
            module4.setEcts(6);
            module4.setDozent("Prof. Weber");
            module4.setLehrstuhl("Algorithmen und Datenstrukturen");
            module4.setRegeltermin("Donnerstag 9:00 Uhr");
            module4.setLiteraturempfehlung("Algorithmenbuch");
            module4.setMinSemester(2);
            module4.setDurchschnittlicheBewertung(4.4);
            module4.setAnzahlBewertungen(19);

            Module module5 = new Module();
            module5.setName("Webentwicklung");
            module5.setBeschreibung("Einführung in die Webentwicklung");
            module5.setEcts(5);
            module5.setDozent("Dr. Wagner");
            module5.setLehrstuhl("Webtechnologien");
            module5.setRegeltermin("Freitag 11:00 Uhr");
            module5.setLiteraturempfehlung("HTML, CSS und JavaScript");
            module5.setMinSemester(3);
            module5.setDurchschnittlicheBewertung(4.0);
            module5.setAnzahlBewertungen(17);

            Module module6 = new Module();
            module6.setName("Netzwerktechnik");
            module6.setBeschreibung("Grundlagen der Netzwerktechnik");
            module6.setEcts(5);
            module6.setDozent("Prof. Klein");
            module6.setLehrstuhl("Netzwerktechnik");
            module6.setRegeltermin("Montag 15:00 Uhr");
            module6.setLiteraturempfehlung("Netzwerkbuch");
            module6.setMinSemester(4);
            module6.setDurchschnittlicheBewertung(4.3);
            module6.setAnzahlBewertungen(16);

            Module module7 = new Module();
            module7.setName("Betriebssysteme");
            module7.setBeschreibung("Einführung in Betriebssysteme");
            module7.setEcts(6);
            module7.setDozent("Prof. Lange");
            module7.setLehrstuhl("Betriebssysteme");
            module7.setRegeltermin("Mittwoch 9:00 Uhr");
            module7.setLiteraturempfehlung("Betriebssysteme-Buch");
            module7.setMinSemester(4);
            module7.setDurchschnittlicheBewertung(4.1);
            module7.setAnzahlBewertungen(18);

            Module module8 = new Module();
            module8.setName("Sicherheit in der Informationstechnik");
            module8.setBeschreibung("Grundlagen der IT-Sicherheit");
            module8.setEcts(7);
            module8.setDozent("Dr. Wolf");
            module8.setLehrstuhl("IT-Sicherheit");
            module8.setRegeltermin("Donnerstag 13:00 Uhr");
            module8.setLiteraturempfehlung("IT-Sicherheitsbuch");
            module8.setMinSemester(5);
            module8.setDurchschnittlicheBewertung(4.6);
            module8.setAnzahlBewertungen(21);

            Module module9 = new Module();
            module9.setName("Softwarequalität");
            module9.setBeschreibung("Qualitätssicherung in der Softwareentwicklung");
            module9.setEcts(6);
            module9.setDozent("Prof. Schneider");
            module9.setLehrstuhl("Softwarequalität");
            module9.setRegeltermin("Freitag 10:00 Uhr");
            module9.setLiteraturempfehlung("Softwarequalitätshandbuch");
            module9.setMinSemester(6);
            module9.setDurchschnittlicheBewertung(4.4);
            module9.setAnzahlBewertungen(20);

            Module module10 = new Module();
            module10.setName("Datenanalyse");
            module10.setBeschreibung("Grundlagen der Datenanalyse");
            module10.setEcts(5);
            module10.setDozent("Dr. Müller");
            module10.setLehrstuhl("Datenanalyse");
            module10.setRegeltermin("Dienstag 10:00 Uhr");
            module10.setLiteraturempfehlung("Datenanalysebuch");
            module10.setMinSemester(5);
            module10.setDurchschnittlicheBewertung(4.3);
            module10.setAnzahlBewertungen(18);

            moduleRepository.save(module1);
            moduleRepository.save(module2);
            moduleRepository.save(module3);
            moduleRepository.save(module4);
            moduleRepository.save(module5);
            moduleRepository.save(module6);
            moduleRepository.save(module7);
            moduleRepository.save(module8);
            moduleRepository.save(module10);
        };
    }
}

