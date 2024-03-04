package byteblaze.sms.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    private LocalDate deadline;
    private Integer priority;
    private LocalDate fulfillmentDate;
    private LocalDate creationDate;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name = "module_id") // Hier die Änderung
    private Long moduleId; // Änderung: Speichert nur die ID des Moduls


    public enum TaskStatus {
        TODO, IN_PROGRESS, DONE
    }
}

