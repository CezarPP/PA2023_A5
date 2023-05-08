package org.example.DAOs;

import java.util.List;

public interface DAO<T> {

    T findById(T id);

    List<T> getAll();

    void create(T t);

    // int update(T t);

    // int delete(T t);
}
