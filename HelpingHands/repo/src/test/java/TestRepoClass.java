import com.helpinghands.domain.Admin;
import com.helpinghands.domain.Eveniment;
import com.helpinghands.domain.Voluntar;
import com.helpinghands.repo.AdminRepo;
import com.helpinghands.repo.EvenimentRepo;
import com.helpinghands.repo.InterestRepo;
import com.helpinghands.repo.VoluntarRepo;
import junit.framework.TestCase;

import java.time.LocalDateTime;
import java.util.Set;

public class TestRepoClass extends TestCase {
    static InterestRepo interestRepo = new InterestRepo();
    static VoluntarRepo voluntarRepo = new VoluntarRepo();
    static AdminRepo adminRepo = new AdminRepo();
    static EvenimentRepo eventRepo = new EvenimentRepo();

    public static void main(String[] args){
        voluntarRepo.add(new Voluntar("user1","0001","user1@gmail.com","Leo","Paleo",1,false, Set.of(interestRepo.getByName("interest2"))));
        voluntarRepo.add(new Voluntar("user2","0002","user2@gmail.com","Neo","Paneo",1,false, Set.of(interestRepo.getByName("interest3"))));
        adminRepo.add(new Admin("housemaster","admin","housemaster@gmail.com","Sefu","Al Mare"));

        /*var user = voluntarRepo.findByCredentials("user1","0000");

        eventRepo.add(new Eveniment("ev1","d1", LocalDateTime.now(), LocalDateTime.now()
        ,"ala",user, "PENDING"));*/
    }
}
