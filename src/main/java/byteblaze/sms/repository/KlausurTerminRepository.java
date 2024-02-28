package byteblaze.sms.repository;

import byteblaze.sms.model.KlausurTermin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KlausurTerminRepository extends JpaRepository<KlausurTermin, Long> {
    List<KlausurTermin> findByModuleId(Long moduleId);

}

