package com.helpinghands.service;

import com.helpinghands.domain.*;
import com.helpinghands.repo.*;
import com.helpinghands.repo.data.EventOrderOption;
import com.helpinghands.service.data.UserInfo;

import java.util.Objects;

public class AppService implements IService {
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

    public AppService(IAdminRepo adminRepo, ICerereSponsorRepo cerereSponsorRepo, IChatRoomRepo chatRoomRepo, IEvenimentRepo evenimentRepo, IInterestRepo interestRepo, IMessageRepo messageRepo, INotificareRepo notificareRepo, IParticipantRepo participantRepo, IPostRepo postRepo, IVoluntarRepo voluntarRepo, IUserSessionRepo userSessionRepo) {
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
        Utilizator u =voluntarRepo.findByCredentials(username, password);
        if(u!=null) {
            var token = userSessionRepo.createToken(u);
            return new UserInfo("Voluntar",u, token);
        }
        u =adminRepo.findByCredentials(username, password);
        if(u!=null) {
            var token = userSessionRepo.createToken(u);
            return new UserInfo("Admin",u, token);
        }
        throw new ServiceException("Invalid username or password");
    }

    @Override
    public void logout(String token) {
        userSessionRepo.close(token);
    }

    @Override
    public Voluntar getVoluntarById(Integer id) throws ServiceException {
        var vol=voluntarRepo.getById(id);
        if(vol==null)
            throw new ServiceException("Invalid Voluntar Id");
        return vol;
    }

    @Override
    public Eveniment getEvenimentById(Integer id) throws ServiceException {
        var evt = evenimentRepo.getById(id);

        if(evt==null){
            throw new ServiceException("Invalid Eveniment Id");
        }

        return evt;
    }

    @Override
    public Iterable<Interest> getInterests() {
        return interestRepo.getAll();
    }

    @Override
    public Interest getInterestByName(String name) throws ServiceException {
        var interest = interestRepo.getByName(name);

        if(interest==null)
            throw new ServiceException("Interest not found");

        return interest;
    }

    @Override
    public void addVoluntarInterest(Voluntar voluntar, Interest interest) {
        voluntar.getInterests().add(interest);
        voluntarRepo.update(voluntar);
    }

    @Override
    public void removeVoluntarInterest(Voluntar voluntar, Interest interest) {
        voluntar.getInterests().removeIf(i-> Objects.equals(i.getName(), interest.getName()));
        voluntarRepo.update(voluntar);
    }

    @Override
    public Eveniment addEvent(Eveniment e) throws ServiceException {
        var initiator = e.getInitiator();
        var organizer = participantRepo.add(new Participant(initiator, true));
        e.getParticipants().add(organizer);
        var ev = evenimentRepo.add(e);
        if(ev==null)
            throw new ServiceException("Could not add event");
        return ev;
    }

    private Participant addParticipant(Voluntar voluntar, Eveniment event, boolean isOrganizer) throws ServiceException {
        var p=participantRepo.add(new Participant(voluntar, isOrganizer));
        if(p==null)
            throw new ServiceException("Could not add participant");
        System.out.println(p);
        event.getParticipants().add(p);
        evenimentRepo.update(event);
        return p;
    }

    @Override
    public Participant addNormalParticipant(Voluntar voluntar, Eveniment event) throws ServiceException {
        return addParticipant(voluntar, event, false);
    }

    @Override
    public Participant addOrganizer(Voluntar voluntar, Eveniment event) throws ServiceException {
        return addParticipant(voluntar, event, true);
    }

    @Override
    public Iterable<Voluntar> getParticipants(Eveniment event) {
        return event.getParticipants().stream().map(Participant::getVoluntar).toList();
    }

    @Override
    public Iterable<Voluntar> getOrganizers(Eveniment event) {
        return event.getParticipants().stream()
                .filter(Participant::isOrganizer)
                .map(Participant::getVoluntar).toList();
    }

    @Override
    public Iterable<Voluntar> getVolunteers(Eveniment event) {
        return event.getParticipants().stream()
                .filter(p->!p.isOrganizer())
                .map(Participant::getVoluntar).toList();
    }

    @Override
    public Eveniment updateEveniment(Eveniment eveniment) {
        evenimentRepo.update(eveniment);
        return eveniment;
    }

    @Override
    public Eveniment deleteVoluntarFromEveniment(Voluntar voluntar, Eveniment eveniment) {
        Boolean isOrganizer = false;
        for(Voluntar v: getOrganizers(eveniment))
            if (v.getId().equals(voluntar.getId())) {
                isOrganizer = true;
                break;
            }
        Participant participant = new Participant(voluntar, isOrganizer);
        participantRepo.remove(participant);
        return eveniment;
    }

    @Override
    public UserSession getUserSession(String token) throws ServiceException {
        var session = userSessionRepo.findByToken(token);
        if(session==null){
            throw new ServiceException("Invalid user token");
        }
        return session;
    }

    @Override
    public Eveniment[] getOrderedEveniments(EventOrderOption orderOption, int page, int itemsPerPage) {
        return evenimentRepo.getOrderedPaged(orderOption, page,itemsPerPage);
    }

    @Override
    public Eveniment[] getEvenimentByOrganizerId(Integer initiatorId){
        return evenimentRepo.getByOrganizer(initiatorId);
    }

}
