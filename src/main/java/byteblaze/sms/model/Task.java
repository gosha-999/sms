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

    @Column(name = "module_id")
    private Long moduleId = null; // Standardwert null


    public enum TaskStatus {
        TODO, IN_PROGRESS, DONE
    }
}

