package byteblaze.sms.model;

import byteblaze.sms.model.Nutzer;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    // Identifiziert, ob der Task zu einem Modul gehört; null für individuelle Nutzer-Tasks
    @Column(nullable = true)
    private Long moduleId;

    // Beziehung zu einem Nutzer, dem der Task gehört; kann null sein für Modul-Tasks, die noch keinem Nutzer zugeordnet sind
    @Column(nullable = true)
    private Long nutzerId;

    public enum TaskStatus {
        TODO, IN_PROGRESS, DONE
    }
}
