package start;

import com.helpinghands.repo.*;
import com.helpinghands.service.AppService;
import com.helpinghands.service.IService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;

@ComponentScan({"com.helpinghands","com.helpinghands.repo"})
@ComponentScan(basePackages = "com.helpinghands.rest_services")
@Configuration
@SpringBootApplication
public class StartRestServices {
    public static void main(String[] args) {
        SpringApplication.run(StartRestServices.class, args);
    }


    static InterestRepo interestRepo = new InterestRepo();
    static VoluntarRepo voluntarRepo = new VoluntarRepo();
    static EvenimentRepo eventRepo = new EvenimentRepo();
    static IParticipantRepo partRepo = new ParticipantRepo();
    @Bean
    static IService getService() {
        return new AppService(null, null, null, eventRepo, interestRepo, null, null, partRepo, null, voluntarRepo);
    }
}
