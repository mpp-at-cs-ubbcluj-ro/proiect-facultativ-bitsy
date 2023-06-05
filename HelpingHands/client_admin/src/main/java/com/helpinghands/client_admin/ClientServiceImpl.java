package com.helpinghands.client_admin;

import com.helpinghands.client_admin.data.UserCredentials;
import com.helpinghands.domain.*;
import com.helpinghands.repo.data.EventOrderOption;
import com.helpinghands.service.IService;
import com.helpinghands.service.ServiceException;
import com.helpinghands.service.data.UserInfo;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public class ClientServiceImpl implements IService{
    public static final String URL = "http://localhost:8080/helpinghands";
    private RestTemplate restTemplate = new RestTemplate();
    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public UserInfo login(String username, String password) throws ServiceException {
        return execute(()->restTemplate.postForObject(URL+"/login",
                new UserCredentials(username, password)
                ,UserInfo.class));
    }

    @Override
    public void logout(String token) {

    }

    @Override
    public Eveniment[] getActualEvenimente() {
        return execute(()->restTemplate.getForObject(URL+"/actualevenimente",
                Eveniment[].class));
    }

    @Override
    public Utilizator createAccount(String username, String password, String email, String nume, String prenume) throws ServiceException {
        return null;
    }

    @Override
    public Iterable<Interest> getInterests() {
        return null;
    }

    @Override
    public Interest getInterestByName(String name) throws ServiceException {
        return null;
    }

    @Override
    public Voluntar getVoluntarById(Integer id) throws ServiceException {
        return null;
    }

    @Override
    public Participant getParticipantById(Integer id) throws ServiceException {
        return null;
    }

    @Override
    public Eveniment getEvenimentById(Integer id) throws ServiceException {
        return null;
    }

    @Override
    public void addVoluntarInterest(Voluntar voluntar, Interest interest) {

    }

    @Override
    public void removeVoluntarInterest(Voluntar voluntar, Interest interest) {

    }

    @Override
    public Eveniment addEvent(Eveniment e) throws ServiceException {
        return null;
    }

    @Override
    public Participant addVolunteer(Voluntar voluntar, Eveniment event) throws ServiceException {
        return null;
    }

    @Override
    public Participant addOrganizer(Voluntar voluntar, Eveniment event) throws ServiceException {
        return null;
    }

    @Override
    public Eveniment deleteParticipantFromEveniment(Participant participant, Eveniment eveniment) {
        return null;
    }

    @Override
    public UserSession getUserSession(String token) throws ServiceException {
        return null;
    }

    @Override
    public Eveniment[] getOrderedEveniments(EventOrderOption orderOption, int page, int itemsPerPage) {
        return new Eveniment[0];
    }

    @Override
    public Participant[] getParticipants(Eveniment event) {
        return new Participant[0];
    }

    @Override
    public Participant[] getOrganizers(Eveniment event) {
        return new Participant[0];
    }

    @Override
    public Participant[] getVolunteers(Eveniment event) {
        return new Participant[0];
    }

    @Override
    public Eveniment updateEveniment(Eveniment eveniment) {
        return null;
    }

    @Override
    public Eveniment[] getEvenimentByOrganizerId(Integer initiatorId) {
        return new Eveniment[0];
    }

    @Override
    public Eveniment[] getEvenimentByVoluntarId(Integer volId) {
        return new Eveniment[0];
    }


    @Override
    public Post adaugaPostare(Post post) {
        return null;
    }

    @Override
    public Iterable<Interest> getVoluntarInterest(Integer volId) throws ServiceException {
        return null;
    }

    @Override
    public CerereSponsor applyForSponsorship(CerereSponsor cerereSponsor) {
        return null;
    }

    @Override
    public SponsorType getSponsorTypeByName(String name) throws ServiceException {
        return null;
    }

    @Override
    public Iterable<SponsorType> getSponsorTypes() {
        return null;
    }

    @Override
    public CerereSponsor[] getPendingSponsorRequests() {
        return new CerereSponsor[0];
    }

    @Override
    public CerereSponsor addCerereSponsor(CerereSponsor cerereSponsor) {
        return null;
    }

    @Override
    public CerereSponsor updateCerereSponsor(CerereSponsor cerereSponsor) {
        return null;
    }

    @Override
    public CerereSponsor getCerereSponsorById(Integer id) {
        return null;
    }

}
