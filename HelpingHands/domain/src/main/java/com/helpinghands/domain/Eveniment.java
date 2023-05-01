package com.helpinghands.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Eveniment implements IEntity {
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    Set<Interest> interests = new HashSet<>();
    Set<Participant> participants = new HashSet<Participant>();
    Voluntar initiator;
    String status; //  PENDING, ACTIVE, FINISHED, CANCELLED

    public Eveniment(String name, String description, LocalDateTime startDate, LocalDateTime endDate, String location, Voluntar initiator, Set<Interest> interests, String status) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.interests = interests;
        this.status = status;
        this.initiator = initiator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<Interest> getInterests() {
        return interests;
    }

    public void setInterests(Set<Interest> interests) {
        this.interests = interests;
    }

    public Set<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }

    public Voluntar getInitiator() {
        return initiator;
    }

    public void setInitiator(Voluntar initiator) {
        this.initiator = initiator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private Integer id;
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id=id;}
}
