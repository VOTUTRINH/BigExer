package com.leap.training.gateway.service;

import com.leap.training.gateway.domain.DocumentType;
import com.leap.training.gateway.repository.DocumentTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link DocumentType}.
 */
@Service
@Transactional
public class DocumentTypeService {

    private final Logger log = LoggerFactory.getLogger(DocumentTypeService.class);

    private final DocumentTypeRepository documentTypeRepository;

    public DocumentTypeService(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    /**
     * Save a documentType.
     *
     * @param documentType the entity to save.
     * @return the persisted entity.
     */
    public Mono<DocumentType> save(DocumentType documentType) {
        log.debug("Request to save DocumentType : {}", documentType);
        return documentTypeRepository.save(documentType);
    }

    /**
     * Partially update a documentType.
     *
     * @param documentType the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<DocumentType> partialUpdate(DocumentType documentType) {
        log.debug("Request to partially update DocumentType : {}", documentType);

        return documentTypeRepository
            .findById(documentType.getId())
            .map(
                existingDocumentType -> {
                    if (documentType.getDescription() != null) {
                        existingDocumentType.setDescription(documentType.getDescription());
                    }

                    return existingDocumentType;
                }
            )
            .flatMap(documentTypeRepository::save);
    }

    /**
     * Get all the documentTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<DocumentType> findAll(Pageable pageable) {
        log.debug("Request to get all DocumentTypes");
        return documentTypeRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of documentTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return documentTypeRepository.count();
    }

    /**
     * Get one documentType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<DocumentType> findOne(Long id) {
        log.debug("Request to get DocumentType : {}", id);
        return documentTypeRepository.findById(id);
    }

    /**
     * Delete the documentType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete DocumentType : {}", id);
        return documentTypeRepository.deleteById(id);
    }
}
