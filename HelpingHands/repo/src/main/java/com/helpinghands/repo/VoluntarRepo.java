package com.helpinghands.repo;

import com.helpinghands.domain.Voluntar;

public class VoluntarRepo extends AbstractUtilizatorRepo<Voluntar> implements IVoluntarRepo {
    public VoluntarRepo() {
        super(Voluntar.class);
    }
}
