package byteblaze.sms.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long moduleId;

    private String beschreibung;
    private int ects;
    private String dozent;
    private String lehrstuhl;
    private String regeltermin;
    private String literaturempfehlung;

    private double durchschnittlicheBewertung;
    private int anzahlBewertungen;
}
