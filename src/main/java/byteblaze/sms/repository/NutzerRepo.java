package byteblaze.sms.repository;
import byteblaze.sms.model.Nutzer;
import org.springframework.data.jpa.repository.JpaRepository;
public interface NutzerRepo extends JpaRepository<Nutzer, Long> {

}
