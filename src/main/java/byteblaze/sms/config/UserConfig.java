package byteblaze.sms.config;

import byteblaze.sms.model.Nutzer;
import byteblaze.sms.repository.NutzerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {
    @Bean
    CommandLineRunner commandLineRunner2(NutzerRepository nutzerRepository){
        return args -> {
            Nutzer nutzer1 = new Nutzer();
            nutzer1.setNutzername("admin");
            nutzer1.setPassword("");
            nutzer1.setEmail("john.doe@example.com");
            nutzer1.setSemester(6);

            Nutzer nutzer2 = new Nutzer();
            nutzer2.setNutzername("JaneDoe");
            nutzer2.setPassword("password2");
            nutzer2.setEmail("jane.doe@example.com");
            nutzer2.setSemester(4);

            Nutzer nutzer3 = new Nutzer();
            nutzer3.setNutzername("AliceSmith");
            nutzer3.setPassword("password3");
            nutzer3.setEmail("alice.smith@example.com");
            nutzer3.setSemester(7);

            Nutzer nutzer4 = new Nutzer();
            nutzer4.setNutzername("BobJohnson");
            nutzer4.setPassword("password4");
            nutzer4.setEmail("bob.johnson@example.com");
            nutzer4.setSemester(5);

            Nutzer nutzer5 = new Nutzer();
            nutzer5.setNutzername("EmilyBrown");
            nutzer5.setPassword("password5");
            nutzer5.setEmail("emily.brown@example.com");
            nutzer5.setSemester(3);




            nutzerRepository.save(nutzer1);
            nutzerRepository.save(nutzer2);
            nutzerRepository.save(nutzer3);
            nutzerRepository.save(nutzer4);
            nutzerRepository.save(nutzer5);
        };
    }
}
