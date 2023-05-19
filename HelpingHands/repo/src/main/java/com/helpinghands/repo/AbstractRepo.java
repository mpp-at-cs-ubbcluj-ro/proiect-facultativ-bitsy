package com.helpinghands.repo;

import com.helpinghands.domain.IEntity;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AbstractRepo<E extends IEntity> implements IRepo<E> {
    protected final Class<E> entityType;

    public AbstractRepo(Class<E> entityType){
        this.entityType = entityType;
    }

    @Override
    public E add(E e) {
        Session.doTransaction((session, tx)->{
            int id = (int)session.save(e);
            tx.commit();
            e.setId(id);
        });
        return e;
    }

    @Override
    public E update(E e) {
        Session.doTransaction((session, tx)->{
            session.update(e);
            tx.commit();
        });
        return e;
    }

    @Override
    public void remove(E e) {
        Session.doTransaction((session, tx)->{
            session.remove(e);
            tx.commit();
        });
    }

    @Override
    public Iterable<E> getAll() {
        AtomicReference<List<E>> result = new AtomicReference<>();
        Session.doTransaction((session, tx)->{
            var cb = session.getCriteriaBuilder();
            var cq = cb.createQuery(entityType);
            var rt = cq.from(entityType);
            result.set(session.createQuery(cq.select(rt)).getResultList());
        });
        return result.get();
    }

    @Override
    public E getById(Integer id) {
        AtomicReference<E> result=new AtomicReference<>();
        result.set(null);
        Session.doTransaction((session, tx)->{
            result.set(entityType.cast(session.get(entityType, id)));
            tx.commit();
        });
        return result.get();
    }
}
