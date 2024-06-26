package byteblaze.sms.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long moduleId;

    private String name;
    @Column(columnDefinition = "LONGTEXT")
    private String beschreibung;
    private int ects;
    private String dozent;
    private String lehrstuhl;
    private String regeltermin;
    private String literaturempfehlung;
    private int minSemester;

    private double durchschnittlicheBewertung;
    private int anzahlBewertungen;

    @ElementCollection
    @CollectionTable(name = "module_bewertungen", joinColumns = @JoinColumn(name = "modul_id"))
    @MapKeyColumn(name = "nutzer_id")
    @Column(name = "rating")
    private Map<Long, Integer> bewertungen = new HashMap<>();

}
