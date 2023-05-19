package com.leap.training.gateway.repository;

import com.leap.training.gateway.domain.DocumentType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the DocumentType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentTypeRepository extends R2dbcRepository<DocumentType, Long>, DocumentTypeRepositoryInternal {
    Flux<DocumentType> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<DocumentType> findAll();

    @Override
    Mono<DocumentType> findById(Long id);

    @Override
    <S extends DocumentType> Mono<S> save(S entity);
}

interface DocumentTypeRepositoryInternal {
    <S extends DocumentType> Mono<S> insert(S entity);
    <S extends DocumentType> Mono<S> save(S entity);
    Mono<Integer> update(DocumentType entity);

    Flux<DocumentType> findAll();
    Mono<DocumentType> findById(Long id);
    Flux<DocumentType> findAllBy(Pageable pageable);
    Flux<DocumentType> findAllBy(Pageable pageable, Criteria criteria);
}
