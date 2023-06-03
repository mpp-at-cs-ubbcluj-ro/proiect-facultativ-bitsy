package com.helpinghands.service;

import com.helpinghands.domain.*;
import com.helpinghands.repo.*;
import com.helpinghands.repo.data.EventOrderOption;
import com.helpinghands.service.data.UserInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

import java.util.*;

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
    private final ISponsorTypeRepo sponsorTypeRepo;

    public AppService(IAdminRepo adminRepo, ICerereSponsorRepo cerereSponsorRepo, IChatRoomRepo chatRoomRepo, IEvenimentRepo evenimentRepo, IInterestRepo interestRepo, IMessageRepo messageRepo, INotificareRepo notificareRepo, IParticipantRepo participantRepo, IPostRepo postRepo, IVoluntarRepo voluntarRepo, IUserSessionRepo userSessionRepo, ISponsorTypeRepo sponsorTypeRepo) {
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
        this.sponsorTypeRepo = sponsorTypeRepo;
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
    public Eveniment[] getActualEvenimente() {
        logger.trace("");
        logger.info("Get evenimente actuale ");

        List<Eveniment> eveniments = new ArrayList<>();

        for(Eveniment eveniment: evenimentRepo.getAll()){
            if(Objects.equals(eveniment.getStatus(), "PENDING")){
                eveniments.add(eveniment);
            }
        }
        logger.traceEntry();
        return eveniments.toArray(Eveniment[]::new);
    }

    @Override
    public Utilizator createAccount(String username, String password, String email, String nume, String prenume) throws ServiceException {
        logger.traceEntry("");
        logger.info("Create Account{}", username + " " + password + " " + email + " " + nume + " " + prenume);
        Voluntar voluntar = new Voluntar(username, password, email, nume, prenume,0,false,new HashSet<>());
        if(Objects.equals(username, "") || Objects.equals(password, "") || Objects.equals(email, "") || Objects.equals(nume, "") || Objects.equals(prenume, ""))
        {
            logger.info("Error{} ","Invalid inputs");
            logger.traceExit();
            throw new ServiceException("Invalid inputs for utilizator");
        }
        voluntarRepo.add(voluntar);
        logger.info("Ok{} ", voluntar);
        logger.traceExit();
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
        logger.traceEntry("");
        logger.info("getParticipantById{} ", id);
        var part = participantRepo.getById(id);
        if(part==null)
        {
            logger.info("Error{} ","Invalid id");
            logger.traceExit();
            throw new ServiceException("Invalid Participant Id");
        }
        logger.info("Ok{} ","Participant found " + part);
        logger.traceExit();
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
    public Participant[] getParticipants(Eveniment event) {
        logger.trace("");
        logger.info("getParticipants {}", event);
        var voluntars = event.getParticipants().stream().toArray(Participant[]::new);
        logger.info("Ok{}", voluntars.length);
        logger.traceExit();
        return voluntars;
    }

    @Override
    public Participant[] getOrganizers(Eveniment event) {
        logger.traceEntry();
        logger.info("getOrganizers{} ", " from " + event.getId());
        var organizers = event.getParticipants().stream()
                .filter(Participant::isOrganizer)
                .toArray(Participant[]::new);
        logger.info("Ok{} ", organizers.length);
        logger.traceExit();
        return organizers;
    }

    @Override
    public Participant[] getVolunteers(Eveniment event) {
        logger.traceEntry("");
        logger.info("getVolunteers{} ", "from " + event.getId());
        var volunteers = event.getParticipants().stream()
                .filter(p->!p.isOrganizer())
                .toArray(Participant[]::new);
        logger.info("Ok{} ",  volunteers.length);
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
        logger.info("Participant sters {} ramasi :", ((Collection<?>)eveniment.getParticipants()).size());
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

    @Override
    public Eveniment[] getEvenimentByVoluntarId(Integer volId){
        logger.traceEntry();
        logger.info("getEvenimentByVoluntarId{} ", volId);
        Eveniment[] eveniments = evenimentRepo.getByVoluntar(volId);
        logger.info("Ok{} ", eveniments.length);
        logger.traceExit();
        return eveniments;
    }

    @Override
    public Post adaugaPostare(Post post) {
        logger.trace("");
        logger.info("addPostare {}", post);
        logger.traceExit();
        return postRepo.add(post);
    }

    @Override
    public Iterable<Interest> getVoluntarInterest(Integer volId) throws ServiceException {
        logger.trace("");
        logger.info("getInterest of voluntar:  {}", volId);
        Voluntar voluntar = this.getVoluntarById(volId);
        Iterable<Interest> interests = voluntar.getInterests();
        logger.info("found interests {}", ((Collection<?>) interests).size());
        logger.traceExit();
        return interests;
    }

    @Override
    public CerereSponsor applyForSponsorship(CerereSponsor cerereSponsor) {
        logger.trace("");
        logger.info("apply for Sponsorship - voluntar: {} ", cerereSponsor.getVolunteer());
        CerereSponsor cerereSponsor1 = this.cerereSponsorRepo.applyforSponsorship(cerereSponsor);
        logger.info("application succesfull {}", cerereSponsor1.getVolunteer() + " " + cerereSponsor1.getStatus());
        logger.traceExit();
        return cerereSponsor1;
    }

    @Override
    public SponsorType getSponsorTypeByName(String name) throws ServiceException {
        logger.trace("");
        logger.info("getSponsorTypeByName {}", name);
        var sponsorType = sponsorTypeRepo.getByName(name);

        if(sponsorType==null){
            logger.info("Error:{}", "Sponsor Type not found");
            logger.traceExit();
            throw new ServiceException("Sponsor Type not found");
        }

        logger.info("Ok:{}", sponsorType);
        logger.traceExit();
        return sponsorType;
    }

    @Override
    public Iterable<SponsorType> getSponsorTypes() {
        logger.trace("");
        logger.info("getSponsorTypes {}");
        Iterable<SponsorType> sponsorTypes = sponsorTypeRepo.getAll();
        logger.info("Ok:{}", ((Collection<?>) sponsorTypes).size());
        logger.traceExit();
        return sponsorTypes;
    }

    @Override
    public CerereSponsor[] getPendingSponsorRequests() {
        logger.trace("");
        logger.info("getPendingSponsorRequests {}");
        Iterable<CerereSponsor> cerereSponsors = cerereSponsorRepo.getAll();
        List<CerereSponsor> cerereSponsorspending = new ArrayList<>();
        for(CerereSponsor cerereSponsor: cerereSponsors){
            if(cerereSponsor.getStatus().equals("pending")){
                cerereSponsorspending.add(cerereSponsor);
            }
        }
        logger.info("Ok:{}", ((Collection<?>) cerereSponsors).size());
        logger.traceExit();
        return  cerereSponsorspending.toArray(CerereSponsor[]::new);
    }

    @Override
    public CerereSponsor addCerereSponsor(CerereSponsor cerereSponsor) {
        logger.trace("");
        logger.info("addCerereSponsor {0}", cerereSponsor);
        CerereSponsor cerereSponsor1 = cerereSponsorRepo.add(cerereSponsor);
        logger.info("Ok:{}", cerereSponsor1);
        logger.traceExit();
        return cerereSponsor1;
    }

    @Override
    public CerereSponsor updateCerereSponsor(CerereSponsor cerereSponsor) {
        logger.trace("");
        logger.info("updateCerereSponsor {0}", cerereSponsor);
        CerereSponsor cerereSponsor1 = cerereSponsorRepo.update(cerereSponsor);
        logger.info("Ok:{}", cerereSponsor1);
        logger.traceExit();
        return cerereSponsor1;
    }

    @Override
    public CerereSponsor getCerereSponsorById(Integer id) {
        logger.trace("");
        logger.info("getCerereSponsorById {}", id);
        CerereSponsor cerereSponsor = cerereSponsorRepo.getById(id);
        logger.info("Ok:{}", cerereSponsor);
        logger.traceExit();
        return cerereSponsor;
    }

}
