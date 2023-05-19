package com.leap.training.gateway.service;

import com.leap.training.gateway.domain.Document;
import com.leap.training.gateway.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Document}.
 */
@Service
@Transactional
public class DocumentService {

    private final Logger log = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    /**
     * Save a document.
     *
     * @param document the entity to save.
     * @return the persisted entity.
     */
    public Mono<Document> save(Document document) {
        log.debug("Request to save Document : {}", document);
        return documentRepository.save(document);
    }

    /**
     * Partially update a document.
     *
     * @param document the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Document> partialUpdate(Document document) {
        log.debug("Request to partially update Document : {}", document);

        return documentRepository
            .findById(document.getId())
            .map(existingDocument -> {
                if (document.getDocumentName() != null) {
                    existingDocument.setDocumentName(document.getDocumentName());
                }
                if (document.getEmployeeId() != null) {
                    existingDocument.setEmployeeId(document.getEmployeeId());
                }

                return existingDocument;
            })
            .flatMap(documentRepository::save);
    }

    /**
     * Get all the documents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Document> findAll(Pageable pageable) {
        log.debug("Request to get all Documents");
        return documentRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of documents available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return documentRepository.count();
    }

    /**
     * Get one document by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Document> findOne(Long id) {
        log.debug("Request to get Document : {}", id);
        return documentRepository.findById(id);
    }

    /**
     * Delete the document by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Document : {}", id);
        return documentRepository.deleteById(id);
    }
}
