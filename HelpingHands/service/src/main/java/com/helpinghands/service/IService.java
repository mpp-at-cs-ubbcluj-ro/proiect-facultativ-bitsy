package com.helpinghands.service;

import com.helpinghands.domain.*;
import com.helpinghands.repo.data.EventOrderOption;
import com.helpinghands.service.data.UserInfo;

public interface IService {
    UserInfo login(String username, String password) throws ServiceException;
    void logout(String token);

    Utilizator createAccount(String username, String password, String email, String nume, String prenume) throws ServiceException;
    Iterable<Interest> getInterests();

    Interest getInterestByName(String name) throws ServiceException;

    Voluntar getVoluntarById(Integer id) throws ServiceException;

    Participant getParticipantById(Integer id) throws ServiceException;

    Eveniment getEvenimentById(Integer id) throws ServiceException;

    void addVoluntarInterest(Voluntar voluntar, Interest interest);
    void removeVoluntarInterest(Voluntar voluntar, Interest interest);
    Eveniment addEvent(Eveniment e) throws ServiceException;
    Participant addVolunteer(Voluntar voluntar, Eveniment event) throws ServiceException;

    Participant addOrganizer(Voluntar voluntar, Eveniment event) throws ServiceException;

    Eveniment deleteParticipantFromEveniment(Participant participant, Eveniment eveniment);

    UserSession getUserSession(String token) throws ServiceException;

    Eveniment[] getOrderedEveniments(EventOrderOption orderOption, int page, int itemsPerPage);
    Participant[] getParticipants(Eveniment event);
    Participant[] getOrganizers(Eveniment event);
    Participant[] getVolunteers(Eveniment event);

    Eveniment updateEveniment(Eveniment eveniment);

    Eveniment[] getEvenimentByOrganizerId(Integer initiatorId);
    Eveniment[] getEvenimentByVoluntarId(Integer volId);
    Post adaugaPostare(Post post);

    Iterable<Interest> getVoluntarInterest(Integer volId) throws ServiceException;

}
