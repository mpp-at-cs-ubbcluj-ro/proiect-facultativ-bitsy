package com.helpinghands.domain;

public class Participant implements IEntity {
    private Voluntar voluntar;
    private boolean organizer;

    public Participant(Voluntar voluntar, boolean organizer) {
        this.voluntar = voluntar;
        this.organizer = organizer;
    }
    public Voluntar getVoluntar() {
        return voluntar;
    }

    public void setVoluntar(Voluntar voluntar) {
        this.voluntar = voluntar;
    }
    public boolean isOrganizer() {
        return organizer;
    }

    public void setOrganizer(boolean organizer) {
        this.organizer = organizer;
    }

    private Integer id;
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id=id;}
}
