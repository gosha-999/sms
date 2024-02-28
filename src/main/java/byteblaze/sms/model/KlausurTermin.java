package byteblaze.sms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class KlausurTermin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long klausurTerminId;

    private Long moduleId;


    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate datum;
    private String klausurName;
    private int maxPlätze;
    private int verbleibendePlätze;

    @ElementCollection
    private List<Long> nutzerIds;

}
