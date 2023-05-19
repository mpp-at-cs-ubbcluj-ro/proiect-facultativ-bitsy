package com.helpinghands.service;

import com.helpinghands.domain.*;

public interface IService {
    Utilizator login(String username, String password) throws ServiceException;
    Iterable<Interest> getInterests();

    Interest getInterestByName(String name) throws ServiceException;

    void addVoluntarInterest(Voluntar voluntar, Interest interest);
    void removeVoluntarInterest(Voluntar voluntar, Interest interest);
    Eveniment addEvent(Eveniment e) throws ServiceException;
    Participant addNormalParticipant(Voluntar voluntar, Eveniment event) throws ServiceException;
    Participant addOrganizer(Voluntar voluntar, Eveniment event) throws ServiceException;

    Iterable<Voluntar> getParticipants(Eveniment event);
    Iterable<Voluntar> getOrganizers(Eveniment event);
    Iterable<Voluntar> getVolunteers(Eveniment event);
}
