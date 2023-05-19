package com.helpinghands.domain;

import javax.persistence.*;

@Entity
@Table(name="SponsorTypes")
public class SponsorType implements IEntity {
    @Column(name="name")
    private String name;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public SponsorType(){}
    public SponsorType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
