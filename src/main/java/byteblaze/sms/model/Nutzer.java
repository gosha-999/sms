package byteblaze.sms.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Entity
@Table
public class Nutzer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long nutzerId; //Eindeutig

    private String nutzername;
    private String password;
    private String details;
    @ManyToMany
    @JoinTable(name = "Merkliste",
            joinColumns = @JoinColumn(name = "nutzer_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id"))
    //SET da keine doppelten Werte aka. Module hinzugefügt werden können
    private Set<Module> merkliste = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "gebuchte_module",
            joinColumns = @JoinColumn(name = "nutzer_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id"))
    //SET da keine doppelten Werte aka. Module hinzugefügt werden können
    private Set<Module> gebuchteModule = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "nutzer_noten", joinColumns = @JoinColumn(name = "nutzer_id"))
    @MapKeyColumn(name = "modul_id")
    @Column(name = "note")
    //KEY VALUE für moduleId und Noten
    private Map<Long, Double> noten = new HashMap<>();
}
