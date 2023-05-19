package com.helpinghands.service;

import com.helpinghands.domain.*;
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

    Iterable<Voluntar> getParticipants(Eveniment event);
    Iterable<Voluntar> getOrganizers(Eveniment event);
    Iterable<Voluntar> getVolunteers(Eveniment event);
}
