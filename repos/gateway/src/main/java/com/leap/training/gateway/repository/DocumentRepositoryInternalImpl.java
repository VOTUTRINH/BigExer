package com.leap.training.gateway.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.leap.training.gateway.domain.Document;
import com.leap.training.gateway.repository.rowmapper.DocumentRowMapper;
import com.leap.training.gateway.repository.rowmapper.DocumentTypeRowMapper;
import com.leap.training.gateway.service.EntityManager;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Document entity.
 */
@SuppressWarnings("unused")
class DocumentRepositoryInternalImpl implements DocumentRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final DocumentTypeRowMapper documenttypeMapper;
    private final DocumentRowMapper documentMapper;

    private static final Table entityTable = Table.aliased("document", EntityManager.ENTITY_ALIAS);
    private static final Table documentTypeTable = Table.aliased("document_type", "documentType");

    public DocumentRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        DocumentTypeRowMapper documenttypeMapper,
        DocumentRowMapper documentMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.documenttypeMapper = documenttypeMapper;
        this.documentMapper = documentMapper;
    }

    @Override
    public Flux<Document> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Document> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Document> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = DocumentSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(DocumentTypeSqlHelper.getColumns(documentTypeTable, "documentType"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(documentTypeTable)
            .on(Column.create("document_type_id", entityTable))
            .equals(Column.create("id", documentTypeTable));

        String select = entityManager.createSelect(selectFrom, Document.class, pageable, criteria);
        String alias = entityTable.getReferenceName().getReference();
        String selectWhere = Optional
            .ofNullable(criteria)
            .map(crit ->
                new StringBuilder(select)
                    .append(" ")
                    .append("WHERE")
                    .append(" ")
                    .append(alias)
                    .append(".")
                    .append(crit.toString())
                    .toString()
            )
            .orElse(select); // TODO remove once https://github.com/spring-projects/spring-data-jdbc/issues/907 will be fixed
        return db.sql(selectWhere).map(this::process);
    }

    @Override
    public Flux<Document> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Document> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Document process(Row row, RowMetadata metadata) {
        Document entity = documentMapper.apply(row, "e");
        entity.setDocumentType(documenttypeMapper.apply(row, "documentType"));
        return entity;
    }

    @Override
    public <S extends Document> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Document> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update Document with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(Document entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class DocumentSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("document_name", table, columnPrefix + "_document_name"));
        columns.add(Column.aliased("employee_id", table, columnPrefix + "_employee_id"));

        columns.add(Column.aliased("document_type_id", table, columnPrefix + "_document_type_id"));
        return columns;
    }
}
