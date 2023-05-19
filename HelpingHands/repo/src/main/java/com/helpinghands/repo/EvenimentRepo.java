package com.helpinghands.repo;

import com.helpinghands.domain.Eveniment;
import com.helpinghands.domain.Voluntar;
import com.helpinghands.repo.data.EventOrderOption;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class EvenimentRepo extends AbstractRepo<Eveniment> implements IEvenimentRepo{
    public EvenimentRepo() {
        super(Eveniment.class);
    }

    @Override
    public Eveniment[] getOrderedPaged(EventOrderOption orderOption, int page, int itemsPerPage) {
        String query = "from Eveniment order by id";
        if(orderOption==EventOrderOption.START_DATE){
            query = "from Eveniment order by startDate";
        } else if (orderOption==EventOrderOption.NAME) {
            query = "from Eveniment order by name";
        }

        String q=query;
        AtomicReference<Eveniment[]> result = new AtomicReference<>();
        Session.doTransaction((session, tx)->{
            result.set(session.createQuery(q, Eveniment.class)
                    .setFirstResult(page*itemsPerPage)
                    .setMaxResults(itemsPerPage)
                    .stream()
                    .filter(ev-> {
                        // to force foreign references to load
                        var x=ev.getInterests().size();
                        return true;
                    })
                    .toArray(Eveniment[]::new));
            tx.commit();
        });
        return result.get();
    }
}
