package com.mandob.base.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, String> {
    <R> Optional<R> findById(String id, Class<R> projection);

    <R> Page<R> findAllBy(Class<R> projection, Pageable pageable);
}
