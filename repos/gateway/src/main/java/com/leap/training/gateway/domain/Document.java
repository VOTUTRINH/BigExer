package com.leap.training.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Document.
 */
@Table("document")
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column("document_name")
    private String documentName;

    @Column("employee_id")
    private Long employeeId;

    @JsonIgnoreProperties(value = { "documents" }, allowSetters = true)
    @Transient
    private DocumentType documentType;

    @Column("document_type_id")
    private Long documentTypeId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Document id(Long id) {
        this.id = id;
        return this;
    }

    public String getDocumentName() {
        return this.documentName;
    }

    public Document documentName(String documentName) {
        this.documentName = documentName;
        return this;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public Document employeeId(Long employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public DocumentType getDocumentType() {
        return this.documentType;
    }

    public Document documentType(DocumentType documentType) {
        this.setDocumentType(documentType);
        this.documentTypeId = documentType != null ? documentType.getId() : null;
        return this;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
        this.documentTypeId = documentType != null ? documentType.getId() : null;
    }

    public Long getDocumentTypeId() {
        return this.documentTypeId;
    }

    public void setDocumentTypeId(Long documentType) {
        this.documentTypeId = documentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Document)) {
            return false;
        }
        return id != null && id.equals(((Document) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Document{" +
            "id=" + getId() +
            ", documentName='" + getDocumentName() + "'" +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
