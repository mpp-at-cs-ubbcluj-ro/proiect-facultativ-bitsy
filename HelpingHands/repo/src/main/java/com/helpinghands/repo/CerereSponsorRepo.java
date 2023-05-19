package com.helpinghands.repo;

import com.helpinghands.domain.CerereSponsor;

public class CerereSponsorRepo extends AbstractRepo<CerereSponsor> implements ICerereSponsorRepo {

    public CerereSponsorRepo(Class<CerereSponsor> entityType) {
        super(entityType);
    }
}
