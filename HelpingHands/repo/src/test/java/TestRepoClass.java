import com.helpinghands.domain.Admin;
import com.helpinghands.domain.Eveniment;
import com.helpinghands.domain.ProfilePic;
import com.helpinghands.domain.Voluntar;
import com.helpinghands.repo.*;
import junit.framework.TestCase;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

public class TestRepoClass extends TestCase {
    static ProfilePicRepo ppRepo;

    static {
        try {
            ppRepo = new ProfilePicRepo("C:\\db\\pp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        /*var pp=new ProfilePic("x.png");
        pp.setId(4);
        ppRepo.add(pp);*/
        //ppRepo.add(3,new byte[2]);
        ppRepo.getImage(3);


        /*var user = voluntarRepo.findByCredentials("user1","0000");

        eventRepo.add(new Eveniment("ev1","d1", LocalDateTime.now(), LocalDateTime.now()
        ,"ala",user, "PENDING"));*/
    }
}
