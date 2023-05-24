package com.helpinghands.service;

import com.helpinghands.domain.*;
import com.helpinghands.repo.*;
import com.helpinghands.repo.data.EventOrderOption;
import com.helpinghands.service.data.UserInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class AppService implements IService {
    private final static Logger logger = LogManager.getLogger();
    private final IAdminRepo adminRepo;
    private final ICerereSponsorRepo cerereSponsorRepo;
    private final IChatRoomRepo chatRoomRepo;
    private final IEvenimentRepo evenimentRepo;
    private final IInterestRepo interestRepo;
    private final IMessageRepo messageRepo;
    private final INotificareRepo notificareRepo;
    private final IParticipantRepo participantRepo;
    private final IPostRepo postRepo;
    private final IVoluntarRepo voluntarRepo;
    private final IUserSessionRepo userSessionRepo;


    public AppService(IAdminRepo adminRepo, ICerereSponsorRepo cerereSponsorRepo, IChatRoomRepo chatRoomRepo, IEvenimentRepo evenimentRepo, IInterestRepo interestRepo, IMessageRepo messageRepo, INotificareRepo notificareRepo, IParticipantRepo participantRepo, IPostRepo postRepo, IVoluntarRepo voluntarRepo, IUserSessionRepo userSessionRepo ) {
        this.adminRepo = adminRepo;
        this.cerereSponsorRepo = cerereSponsorRepo;
        this.chatRoomRepo = chatRoomRepo;
        this.evenimentRepo = evenimentRepo;
        this.interestRepo = interestRepo;
        this.messageRepo = messageRepo;
        this.notificareRepo = notificareRepo;
        this.participantRepo = participantRepo;
        this.postRepo = postRepo;
        this.voluntarRepo = voluntarRepo;
        this.userSessionRepo = userSessionRepo;

    }

    @Override
    public UserInfo login(String username, String password) throws ServiceException {
        logger.trace("");
        logger.info("Login { "+username + " " + password+ " }");

        Utilizator u =voluntarRepo.findByCredentials(username, password);
        if(u!=null) {
            var token = userSessionRepo.createToken(u);
            logger.info("Ok:{}",token);
            logger.traceExit();
            return new UserInfo("Voluntar",u, token);
        }
        u =adminRepo.findByCredentials(username, password);
        if(u!=null) {
            var token = userSessionRepo.createToken(u);
            logger.info("Ok:{}",token);
            logger.traceExit();
            return new UserInfo("Admin",u, token);
        }
        logger.info("Error:{}", "invalid credentials");
        logger.traceExit();
        throw new ServiceException("Invalid username or password");
    }

    @Override
    public void logout(String token) {
        logger.trace("");
        logger.info("Logout{}", token);
        userSessionRepo.close(token);
        logger.info("Ok:{}", "Logout succesful");
        logger.traceExit();
    }

    @Override
    public Utilizator createAccount(String username, String password, String email, String nume, String prenume) throws ServiceException {

        Voluntar voluntar = new Voluntar(username, password, email, nume, prenume,0,false,new HashSet<>());
        if(Objects.equals(username, "") || Objects.equals(password, "") || Objects.equals(email, "") || Objects.equals(nume, "") || Objects.equals(prenume, ""))
            throw new ServiceException("Invalid inputs for utilizator");

        voluntarRepo.add(voluntar);
        return voluntar;
    }

    @Override
    public Voluntar getVoluntarById(Integer id) throws ServiceException {
        logger.trace("");
        logger.info("GetVoluntarById {} ", id);
        var vol=voluntarRepo.getById(id);
        if(vol==null){
            logger.info("Error:{}", " Voluntar found by id");
            logger.traceExit();
            throw new ServiceException("Invalid Voluntar Id");
        }
        logger.info("Ok:{}", vol);
        logger.traceExit();
        return vol;
    }

    @Override
    public Participant getParticipantById(Integer id) throws ServiceException {
        var part = participantRepo.getById(id);
        if(part==null)
            throw new ServiceException("Invalid Participant Id");
        return part;
    }

    @Override
    public Eveniment getEvenimentById(Integer id) throws ServiceException {
        logger.trace("");
        logger.info("GetEvenimentId {} ", id);

        var evt = evenimentRepo.getById(id);

        if(evt==null){
            logger.info("Error:{}", "Invalid Eveniment Id");
            logger.traceExit();
            throw new ServiceException("Invalid Eveniment Id");
        }

        logger.info("Ok:{}", evt);
        logger.traceExit();

        return evt;
    }

    @Override
    public Iterable<Interest> getInterests() {
        logger.trace("");
        logger.info("getInterests {}");
        Iterable<Interest> interests = interestRepo.getAll();
        logger.info("Ok:{}", ((Collection<?>) interests).size());
        logger.traceExit();
        return interests;
    }

    @Override
    public Interest getInterestByName(String name) throws ServiceException {
        logger.trace("");
        logger.info("getInterestsByName {}", name);
        var interest = interestRepo.getByName(name);

        if(interest==null){
            logger.info("Error:{}", "Interest not found");
            logger.traceExit();
            throw new ServiceException("Interest not found");
        }

        logger.info("Ok:{}", interest);
        logger.traceExit();
        return interest;
    }

    @Override
    public void addVoluntarInterest(Voluntar voluntar, Interest interest) {
        logger.trace("");
        logger.info("addVoluntarInterest {}", interest + " to " + voluntar);
        voluntar.getInterests().add(interest);
        voluntarRepo.update(voluntar);
        logger.info("Ok:{}", voluntar);
        logger.traceExit();
    }

    @Override
    public void removeVoluntarInterest(Voluntar voluntar, Interest interest) {
        logger.trace("");
        logger.info("removeVoluntarInterest {}", interest + " from " + voluntar);
        voluntar.getInterests().removeIf(i-> Objects.equals(i.getName(), interest.getName()));
        voluntarRepo.update(voluntar);
        logger.info("Ok:{}", voluntar);
        logger.traceExit();
    }

    @Override
    public Eveniment addEvent(Eveniment e) throws ServiceException {
        logger.trace("");
        logger.info("addEvent {}", e);

        var initiator = e.getInitiator();
        var organizer = participantRepo.add(new Participant(initiator, true));
        e.getParticipants().add(organizer);
        var ev = evenimentRepo.add(e);
        if(ev==null){
            logger.info("Error:{}", "Could not add event!");
            logger.traceExit();
            throw new ServiceException("Could not add event");
        }
        logger.info("Ok:{}", ev);
        logger.traceExit();
        return ev;
    }

    private Participant addParticipant(Voluntar voluntar, Eveniment event, boolean isOrganizer) throws ServiceException {
        logger.trace("");
        logger.info("addParticipantt {}", voluntar + "to" + event.getId());

        var p=participantRepo.add(new Participant(voluntar, isOrganizer));
        if(p==null){
            logger.info("Error:{}", "Could not add participant");
            logger.traceExit();
            throw new ServiceException("Could not add participant");
        }

        System.out.println(p);
        event.getParticipants().add(p);
        evenimentRepo.update(event);
        logger.info("Ok:{}", p);
        logger.traceExit();
        return p;
    }

    @Override
    public Participant addVolunteer(Voluntar voluntar, Eveniment event) throws ServiceException {
        logger.trace("");
        logger.info("addVolunteer {}", voluntar + "to" + event.getId());
        logger.traceExit();
        return addParticipant(voluntar, event, false);
    }

    @Override
    public Participant addOrganizer(Voluntar voluntar, Eveniment event) throws ServiceException {
        logger.trace("");
        logger.info("addOrganizer {}", voluntar + "to" + event.getId());
        logger.traceExit();
        return addParticipant(voluntar, event, true);
    }

    @Override
    public Iterable<Voluntar> getParticipants(Eveniment event) {
        logger.trace("");
        logger.info("getParticipants {}");
        Iterable<Voluntar> voluntars = event.getParticipants().stream().map(Participant::getVoluntar).toList();
        logger.info("Ok{}", ((Collection<?>) voluntars).size());
        logger.traceExit();
        return voluntars;
    }

    @Override
    public Iterable<Voluntar> getOrganizers(Eveniment event) {
        logger.traceEntry();
        logger.info("getOrganizers{} ", " from " + event.getId());
        Iterable<Voluntar> organizers = event.getParticipants().stream()
                .filter(Participant::isOrganizer)
                .map(Participant::getVoluntar).toList();
        logger.info("Ok{} ", ((Collection<?>) organizers).size());
        logger.traceExit();
        return organizers;
    }

    @Override
    public Iterable<Voluntar> getVolunteers(Eveniment event) {
        logger.traceEntry("");
        logger.info("getVolunteers{} ", "from " + event.getId());
        Iterable<Voluntar> volunteers = event.getParticipants().stream()
                .filter(p->!p.isOrganizer())
                .map(Participant::getVoluntar).toList();
        logger.info("Ok{} ",  ((Collection<?>) volunteers).size());
        logger.traceExit();
        return volunteers;
    }

    @Override
    public Eveniment updateEveniment(Eveniment eveniment) {
        logger.traceEntry("");
        logger.info("updateEveniment{} ", eveniment.getId());
        evenimentRepo.update(eveniment);
        logger.info("Ok{} ");
        logger.traceExit();
        return eveniment;
    }

    @Override
    public Eveniment deleteParticipantFromEveniment(Participant participant, Eveniment eveniment) {
        logger.traceEntry("");
        logger.info("removeParticipant{}", "from " + eveniment.getId()+" total:"+ ((Collection<?>)eveniment.getParticipants()).size());
        eveniment.getParticipants().removeIf(p-> Objects.equals(p.getId(), participant.getId()));
        evenimentRepo.update(eveniment);
        logger.info("Participant sters{} ramasi :", ((Collection<?>)eveniment.getParticipants()).size());
        logger.traceExit();
        return eveniment;
    }

    @Override
    public UserSession getUserSession(String token) throws ServiceException {
        logger.traceEntry("");
        logger.info("GetUserSession{}");
        var session = userSessionRepo.findByToken(token);
        if(session==null){
            logger.info("Error{] ", "Invalid token");
            logger.traceExit();
            throw new ServiceException("Invalid user token");
        }
        logger.info("Ok{} ", "Successful");
        logger.traceExit();
        return session;
    }

    @Override
    public Eveniment[] getOrderedEveniments(EventOrderOption orderOption, int page, int itemsPerPage) {
        logger.traceEntry("");
        logger.info("getOrderedEveniments{} ", orderOption + " page: " + page + " itmes/page: " + itemsPerPage);
        Eveniment[] eveniments = evenimentRepo.getOrderedPaged(orderOption, page,itemsPerPage);
        logger.info("Ok{} ", eveniments.length);
        logger.traceExit();
        return eveniments;
    }

    @Override
    public Eveniment[] getEvenimentByOrganizerId(Integer initiatorId){
        logger.traceEntry();
        logger.info("getEvenimentByOrganizerId{} ", initiatorId);
        Eveniment[] eveniments = evenimentRepo.getByOrganizer(initiatorId);
        logger.info("Ok{} ", eveniments.length);
        logger.traceExit();
        return eveniments;
    }

}
