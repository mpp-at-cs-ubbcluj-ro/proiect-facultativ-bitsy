import com.helpinghands.domain.Eveniment;
import com.helpinghands.domain.Voluntar;
import com.helpinghands.repo.*;
import com.helpinghands.service.AppService;
import com.helpinghands.service.IService;
import com.helpinghands.service.ServiceException;
import junit.framework.TestCase;

import java.time.LocalDateTime;

public class TestServClass extends TestCase {
    static InterestRepo interestRepo = new InterestRepo();
    static VoluntarRepo voluntarRepo = new VoluntarRepo();
    static EvenimentRepo eventRepo = new EvenimentRepo();
    static IParticipantRepo partRepo = new ParticipantRepo();
    static IService service = new AppService(null, null, null, eventRepo, interestRepo, null, null, partRepo, null, voluntarRepo);

    public static void main(String[] args) throws ServiceException {
        var u=service.login("user1","0000");
        var v=(Voluntar)u;
        System.out.println(u);

        //var ev=new Eveniment("a","b",LocalDateTime.now(),LocalDateTime.now(),"L",v,"PENDING");
        var ev=eventRepo.getById(2);
        System.out.println(ev);


        //var ev=eventRepo.getById(17);
        //service.getVolunteers(ev).forEach(System.out::println);
    }
}