package com.helpinghands.repo;

import com.helpinghands.domain.Eveniment;
import com.helpinghands.domain.Voluntar;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class EvenimentRepo extends AbstractRepo<Eveniment> implements IEvenimentRepo{
    public EvenimentRepo() {
        super(Eveniment.class);
    }
}
