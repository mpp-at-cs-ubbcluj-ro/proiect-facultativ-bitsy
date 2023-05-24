package com.helpinghands.domain;

public class CerereSponsor implements IEntity {
    private Voluntar volunteer;
    private String cifFirma;
    private String telefon;
    private String adresaSediului;
    private String numeFirma;

    private SponsorType sponsorType;

    public CerereSponsor(Voluntar volunteer, String cifFirma, String telefon, String adresaSediului, String numeFirma, SponsorType sponsorType, Integer id) {
        this.volunteer = volunteer;
        this.cifFirma = cifFirma;
        this.telefon = telefon;
        this.adresaSediului = adresaSediului;
        this.numeFirma = numeFirma;
        this.sponsorType = sponsorType;
        this.id = id;
    }

    public Voluntar getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Voluntar volunteer) {
        this.volunteer = volunteer;
    }

    public String getCifFirma() {
        return cifFirma;
    }

    public void setCifFirma(String cifFirma) {
        this.cifFirma = cifFirma;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getAdresaSediului() {
        return adresaSediului;
    }

    public void setAdresaSediului(String adresaSediului) {
        this.adresaSediului = adresaSediului;
    }

    public String getNumeFirma() {
        return numeFirma;
    }

    public void setNumeFirma(String numeFirma) {
        this.numeFirma = numeFirma;
    }

    public SponsorType getSponsorType() {
        return sponsorType;
    }

    public void setSponsorType(SponsorType sponsorType) {
        this.sponsorType = sponsorType;
    }

    private Integer id;
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id=id;}
}
