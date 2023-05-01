import com.helpinghands.domain.*;
import com.helpinghands.repo.HibernateUtil;
import com.helpinghands.repo.Session;
import com.helpinghands.repo.VoluntarRepo;
import junit.framework.TestCase;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.Set;

public class TestClass extends TestCase {
    public static void main(String[] args){
        var i1 = new Interest("a");
        var i2 = new Interest("c");
        var user = new Voluntar("user1","0000","x@gmail.com","Ramon","Laicu"
            ,15, false, Set.of(
                    new Interest("b"),
                    i2
                ));
        var event = new Eveniment("event1","descri",
                LocalDateTime.now(), LocalDateTime.now(),"loc",
                user, Set.of(i1,i2), "PENDING");
        event.getParticipants().add(new Participant(user, false));

        var chatroom = new ChatRoom(event, false);
        var message = new Message("tralala", user, chatroom);

        var cerere = new CerereSponsor(user, "cif","123456789","adr","nume");

        var volRepo = new VoluntarRepo();
        volRepo.add(user);


        var u = volRepo.getById(15);
        System.out.println(u);
        volRepo.remove(u);

        Session.closeFactory();
    }
}
