package com.helpinghands.repo;

import com.helpinghands.domain.IEntity;

public interface IRepo<E extends IEntity> {
    void add(E e);
    void update(E e);
    void remove(E e);
    Iterable<E> getAll();
    E getById(Integer id);
}
