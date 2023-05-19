package com.leap.training.gateway.repository.rowmapper;

import com.leap.training.gateway.domain.Document;
import com.leap.training.gateway.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Document}, with proper type conversions.
 */
@Service
public class DocumentRowMapper implements BiFunction<Row, String, Document> {

    private final ColumnConverter converter;

    public DocumentRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Document} stored in the database.
     */
    @Override
    public Document apply(Row row, String prefix) {
        Document entity = new Document();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDocumentName(converter.fromRow(row, prefix + "_document_name", String.class));
        entity.setEmployeeId(converter.fromRow(row, prefix + "_employee_id", Long.class));
        entity.setDocumentTypeId(converter.fromRow(row, prefix + "_document_type_id", Long.class));
        return entity;
    }
}
