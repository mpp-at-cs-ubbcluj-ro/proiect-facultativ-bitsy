package com.helpinghands.repo;

import com.helpinghands.domain.CerereSponsor;
import com.helpinghands.domain.SponsorType;
import com.helpinghands.domain.Voluntar;

public class CerereSponsorRepo extends AbstractRepo<CerereSponsor> implements ICerereSponsorRepo {

    public CerereSponsorRepo() {
        super(CerereSponsor.class);
    }
    @Override
    public void applyforSponsorship(CerereSponsor cerereSponsor) {
        this.add(cerereSponsor);
    }
}
