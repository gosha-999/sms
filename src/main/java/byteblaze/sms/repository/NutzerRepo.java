package byteblaze.sms.repository;
import byteblaze.sms.model.Nutzer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NutzerRepo extends JpaRepository<Nutzer, Long> {

    boolean existsByGebuchteModuleModuleId(Long moduleId);

    Nutzer findByNutzernameAndPassword(String nutzername, String password);
}
