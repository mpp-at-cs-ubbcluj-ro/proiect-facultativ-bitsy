package com.helpinghands.repo;

import com.helpinghands.domain.IEntity;

import java.util.concurrent.atomic.AtomicReference;

public class AbstractRepo<E extends IEntity> implements IRepo<E> {
    private final Class<E> entityType;

    public AbstractRepo(Class<E> entityType){
        this.entityType = entityType;
    }

    @Override
    public void add(E e) {
        Session.doTransaction((session, tx)->{
            session.save(e);
            tx.commit();
        });
    }

    @Override
    public void update(E e) {
        Session.doTransaction((session, tx)->{
            session.update(e);
            tx.commit();
        });
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
        return null;
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
