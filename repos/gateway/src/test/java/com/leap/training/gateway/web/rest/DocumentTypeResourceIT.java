package com.leap.training.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.leap.training.gateway.IntegrationTest;
import com.leap.training.gateway.domain.DocumentType;
import com.leap.training.gateway.repository.DocumentTypeRepository;
import com.leap.training.gateway.service.EntityManager;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link DocumentTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class DocumentTypeResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/document-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private DocumentType documentType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentType createEntity(EntityManager em) {
        DocumentType documentType = new DocumentType().description(DEFAULT_DESCRIPTION);
        return documentType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentType createUpdatedEntity(EntityManager em) {
        DocumentType documentType = new DocumentType().description(UPDATED_DESCRIPTION);
        return documentType;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(DocumentType.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        documentType = createEntity(em);
    }

    @Test
    void createDocumentType() throws Exception {
        int databaseSizeBeforeCreate = documentTypeRepository.findAll().collectList().block().size();
        // Create the DocumentType
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(documentType))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll().collectList().block();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentType testDocumentType = documentTypeList.get(documentTypeList.size() - 1);
        assertThat(testDocumentType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createDocumentTypeWithExistingId() throws Exception {
        // Create the DocumentType with an existing ID
        documentType.setId(1L);

        int databaseSizeBeforeCreate = documentTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(documentType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll().collectList().block();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDocumentTypes() {
        // Initialize the database
        documentTypeRepository.save(documentType).block();

        // Get all the documentTypeList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(documentType.getId().intValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getDocumentType() {
        // Initialize the database
        documentTypeRepository.save(documentType).block();

        // Get the documentType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, documentType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(documentType.getId().intValue()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingDocumentType() {
        // Get the documentType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewDocumentType() throws Exception {
        // Initialize the database
        documentTypeRepository.save(documentType).block();

        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().collectList().block().size();

        // Update the documentType
        DocumentType updatedDocumentType = documentTypeRepository.findById(documentType.getId()).block();
        updatedDocumentType.description(UPDATED_DESCRIPTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedDocumentType.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedDocumentType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll().collectList().block();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
        DocumentType testDocumentType = documentTypeList.get(documentTypeList.size() - 1);
        assertThat(testDocumentType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingDocumentType() throws Exception {
        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().collectList().block().size();
        documentType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, documentType.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(documentType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll().collectList().block();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDocumentType() throws Exception {
        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().collectList().block().size();
        documentType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(documentType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll().collectList().block();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDocumentType() throws Exception {
        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().collectList().block().size();
        documentType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(documentType))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll().collectList().block();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDocumentTypeWithPatch() throws Exception {
        // Initialize the database
        documentTypeRepository.save(documentType).block();

        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().collectList().block().size();

        // Update the documentType using partial update
        DocumentType partialUpdatedDocumentType = new DocumentType();
        partialUpdatedDocumentType.setId(documentType.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDocumentType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll().collectList().block();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
        DocumentType testDocumentType = documentTypeList.get(documentTypeList.size() - 1);
        assertThat(testDocumentType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void fullUpdateDocumentTypeWithPatch() throws Exception {
        // Initialize the database
        documentTypeRepository.save(documentType).block();

        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().collectList().block().size();

        // Update the documentType using partial update
        DocumentType partialUpdatedDocumentType = new DocumentType();
        partialUpdatedDocumentType.setId(documentType.getId());

        partialUpdatedDocumentType.description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDocumentType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll().collectList().block();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
        DocumentType testDocumentType = documentTypeList.get(documentTypeList.size() - 1);
        assertThat(testDocumentType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingDocumentType() throws Exception {
        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().collectList().block().size();
        documentType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, documentType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(documentType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll().collectList().block();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDocumentType() throws Exception {
        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().collectList().block().size();
        documentType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(documentType))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll().collectList().block();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDocumentType() throws Exception {
        int databaseSizeBeforeUpdate = documentTypeRepository.findAll().collectList().block().size();
        documentType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(documentType))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DocumentType in the database
        List<DocumentType> documentTypeList = documentTypeRepository.findAll().collectList().block();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDocumentType() {
        // Initialize the database
        documentTypeRepository.save(documentType).block();

        int databaseSizeBeforeDelete = documentTypeRepository.findAll().collectList().block().size();

        // Delete the documentType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, documentType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<DocumentType> documentTypeList = documentTypeRepository.findAll().collectList().block();
        assertThat(documentTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
