package com.helpinghands.service;

import com.helpinghands.domain.*;
import com.helpinghands.repo.data.EventOrderOption;
import com.helpinghands.service.data.UserInfo;

public interface IService {
    UserInfo login(String username, String password) throws ServiceException;
    void logout(String token);
    Iterable<Interest> getInterests();

    Interest getInterestByName(String name) throws ServiceException;

    void addVoluntarInterest(Voluntar voluntar, Interest interest);
    void removeVoluntarInterest(Voluntar voluntar, Interest interest);
    Eveniment addEvent(Eveniment e) throws ServiceException;
    Participant addNormalParticipant(Voluntar voluntar, Eveniment event) throws ServiceException;
    Participant addOrganizer(Voluntar voluntar, Eveniment event) throws ServiceException;

    UserSession getUserSession(String token) throws ServiceException;

    Eveniment[] getOrderedEveniments(EventOrderOption orderOption, int page, int itemsPerPage);
    Iterable<Voluntar> getParticipants(Eveniment event);
    Iterable<Voluntar> getOrganizers(Eveniment event);
    Iterable<Voluntar> getVolunteers(Eveniment event);

    Eveniment updateEveniment(Eveniment eveniment);

    Eveniment deleteVoluntarFromEveniment(Voluntar voluntar, Eveniment eveniment);
}
