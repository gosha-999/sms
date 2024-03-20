package byteblaze.sms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class KlausurTermin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long klausurTerminId;

    private Long moduleId;



    private LocalDate datum;
    private String klausurName;
    private int maxPlätze;
    private int verbleibendePlätze;

    @ElementCollection
    private List<Long> gebuchtVonNutzerIds = new ArrayList<>();

}
