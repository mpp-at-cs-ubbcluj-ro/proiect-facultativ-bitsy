package com.helpinghands.domain;

import javax.persistence.*;

@Entity
@Table(name= "UserSessions")
public class UserSession implements IEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String token;
    @ManyToOne(targetEntity = Voluntar.class,
            cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinColumn(name="idUser")
    private Utilizator utilizator;
    private String type;

    public UserSession() { }

    public UserSession(String token, Utilizator utilizator, String type) {
        this.token = token;
        this.utilizator = utilizator;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Utilizator getUtilizator() {
        return utilizator;
    }

    public void setUtilizator(Utilizator utilizator) {
        this.utilizator = utilizator;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id=id;
    }
}
