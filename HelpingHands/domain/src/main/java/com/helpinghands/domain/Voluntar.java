package com.helpinghands.domain;

import java.util.HashSet;
import java.util.Set;

public class Voluntar extends Utilizator {
    Set<Interest> interests = new HashSet<>();
    int xpPoints;
    boolean sponsor;
    boolean activeSponsor;
    Set<SponsorType> sponsorTypes = new HashSet<>();
    Set<Eveniment> sponsoredEvents = new HashSet<>();

    public Voluntar() { }

    public Voluntar(String username, String password, String email, String nume, String prenume,
                    int xpPoints, boolean isSponsor, Set<Interest> interests) {
        super(username, password, email, nume, prenume);
        this.xpPoints = xpPoints;
        this.sponsor = isSponsor;
        this.interests = interests;
    }

    public void setSponsor(boolean sponsor) {
        this.sponsor = sponsor;
    }

    public boolean isActiveSponsor() {
        return activeSponsor;
    }

    public void setActiveSponsor(boolean activeSponsor) {
        this.activeSponsor = activeSponsor;
    }

    public Set<SponsorType> getSponsorTypes() {
        return sponsorTypes;
    }

    public void setSponsorTypes(Set<SponsorType> sponsorTypes) {
        this.sponsorTypes = sponsorTypes;
    }

    public Set<Eveniment> getSponsoredEvents() {
        return sponsoredEvents;
    }

    public void setSponsoredEvents(Set<Eveniment> sponsoredEvents) {
        this.sponsoredEvents = sponsoredEvents;
    }

    public int getXpPoints() {
        return xpPoints;
    }

    public void setXpPoints(int xpPoints) {
        this.xpPoints = xpPoints;
    }

    public boolean isSponsor() {
        return sponsor;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public void setInterests(Set<Interest> interests) {
        this.interests = interests;
    }


    @Override
    public String toString() {
        return "Voluntar{" +
                "interests=" + interests +
                ", xpPoints=" + xpPoints +
                ", sponsor=" + sponsor +
                ", activeSponsor=" + activeSponsor +
                ", sponsorTypes=" + sponsorTypes +
                ", sponsoredEvents=" + sponsoredEvents +
                '}';
    }
}
