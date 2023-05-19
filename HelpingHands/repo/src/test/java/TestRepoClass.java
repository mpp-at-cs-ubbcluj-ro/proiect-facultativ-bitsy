import com.helpinghands.domain.Eveniment;
import com.helpinghands.repo.EvenimentRepo;
import com.helpinghands.repo.InterestRepo;
import com.helpinghands.repo.VoluntarRepo;
import junit.framework.TestCase;

import java.time.LocalDateTime;

public class TestRepoClass extends TestCase {
    static InterestRepo interestRepo = new InterestRepo();
    static VoluntarRepo voluntarRepo = new VoluntarRepo();
    static EvenimentRepo eventRepo = new EvenimentRepo();

    public static void main(String[] args){
        var user = voluntarRepo.findByCredentials("user1","0000");

        eventRepo.add(new Eveniment("ev1","d1", LocalDateTime.now(), LocalDateTime.now()
        ,"ala",user, "PENDING"));
    }
}
