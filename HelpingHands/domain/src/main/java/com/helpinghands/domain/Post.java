package com.helpinghands.domain;

import java.time.LocalDateTime;

public class Post implements IEntity {
    private String descriere;
    private LocalDateTime data;
    Eveniment eveniment;
    Utilizator author;

    public Post(String descriere, LocalDateTime data, Eveniment eveniment, Utilizator author) {
        this.descriere = descriere;
        this.data = data;
        this.eveniment = eveniment;
        this.author = author;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Eveniment getEveniment() {
        return eveniment;
    }

    public void setEveniment(Eveniment eveniment) {
        this.eveniment = eveniment;
    }

    public Utilizator getAuthor() {
        return author;
    }

    public void setAuthor(Utilizator author) {
        this.author = author;
    }

    private Integer id;
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id=id;}
}
