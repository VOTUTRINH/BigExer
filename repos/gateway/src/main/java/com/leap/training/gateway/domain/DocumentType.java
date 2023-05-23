package com.leap.training.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A DocumentType.
 */
@Table("document_type")
public class DocumentType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column("description")
    private String description;

    @Transient
    @JsonIgnoreProperties(value = { "documentType" }, allowSetters = true)
    private Set<Document> documents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentType id(Long id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public DocumentType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public DocumentType documents(Set<Document> documents) {
        this.setDocuments(documents);
        return this;
    }

    public DocumentType addDocuments(Document document) {
        this.documents.add(document);
        document.setDocumentType(this);
        return this;
    }

    public DocumentType removeDocuments(Document document) {
        this.documents.remove(document);
        document.setDocumentType(null);
        return this;
    }

    public void setDocuments(Set<Document> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setDocumentType(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setDocumentType(this));
        }
        this.documents = documents;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentType)) {
            return false;
        }
        return id != null && id.equals(((DocumentType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentType{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
