package com.leap.training.gateway.repository.rowmapper;

import com.leap.training.gateway.domain.DocumentType;
import com.leap.training.gateway.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link DocumentType}, with proper type conversions.
 */
@Service
public class DocumentTypeRowMapper implements BiFunction<Row, String, DocumentType> {

    private final ColumnConverter converter;

    public DocumentTypeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link DocumentType} stored in the database.
     */
    @Override
    public DocumentType apply(Row row, String prefix) {
        DocumentType entity = new DocumentType();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        return entity;
    }
}
