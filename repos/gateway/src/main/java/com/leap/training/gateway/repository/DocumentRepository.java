package com.leap.training.gateway.repository;

import com.leap.training.gateway.domain.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Document entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentRepository extends R2dbcRepository<Document, Long>, DocumentRepositoryInternal {
    Flux<Document> findAllBy(Pageable pageable);

    @Query("SELECT * FROM document entity WHERE entity.document_type_id = :id")
    Flux<Document> findByDocumentType(Long id);

    @Query("SELECT * FROM document entity WHERE entity.document_type_id IS NULL")
    Flux<Document> findAllWhereDocumentTypeIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Document> findAll();

    @Override
    Mono<Document> findById(Long id);

    @Override
    <S extends Document> Mono<S> save(S entity);
}

interface DocumentRepositoryInternal {
    <S extends Document> Mono<S> insert(S entity);
    <S extends Document> Mono<S> save(S entity);
    Mono<Integer> update(Document entity);

    Flux<Document> findAll();
    Mono<Document> findById(Long id);
    Flux<Document> findAllBy(Pageable pageable);
    Flux<Document> findAllBy(Pageable pageable, Criteria criteria);
}
