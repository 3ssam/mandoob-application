package com.mandob.base.service;

import com.mandob.base.Utils.PageRequestVM;
import com.mandob.base.domain.BaseEntity;
import com.mandob.base.repository.BaseRepository;
import org.springframework.data.domain.Page;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

public abstract class BaseService<T extends BaseEntity> {
    public T getReference(String id) {
        return getRepository().getOne(id);
    }

    public T findById(String id) {
        return getRepository().findById(id).orElseThrow(noSuchElement(id));
    }

    public <R> R findById(String id, Class<R> projection) {
        return getRepository().findById(id, projection).orElseThrow(noSuchElement(id));
    }

    public <R> Page<R> findAll(Class<R> projection, PageRequestVM pr) {
        return getRepository().findAllBy(projection, pr.build());
    }

    protected Supplier<NoSuchElementException> noSuchElement(String id) {
        String entityName = this.getClass().getSimpleName().replaceAll("Service[a-zA-Z]*", "");
        return () -> new NoSuchElementException("Can't find " + entityName + " with Id: " + id);
    }

    protected abstract BaseRepository<T> getRepository();
}
