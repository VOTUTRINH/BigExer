package com.leap.training.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.leap.training.gateway.IntegrationTest;
import com.leap.training.gateway.domain.Document;
import com.leap.training.gateway.repository.DocumentRepository;
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
 * Integration tests for the {@link DocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class DocumentResourceIT {

    private static final String DEFAULT_DOCUMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_EMPLOYEE_ID = 1L;
    private static final Long UPDATED_EMPLOYEE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Document document;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createEntity(EntityManager em) {
        Document document = new Document().documentName(DEFAULT_DOCUMENT_NAME).employeeId(DEFAULT_EMPLOYEE_ID);
        return document;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createUpdatedEntity(EntityManager em) {
        Document document = new Document().documentName(UPDATED_DOCUMENT_NAME).employeeId(UPDATED_EMPLOYEE_ID);
        return document;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Document.class).block();
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
        document = createEntity(em);
    }

    @Test
    void createDocument() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().collectList().block().size();
        // Create the Document
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(document))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll().collectList().block();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate + 1);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getDocumentName()).isEqualTo(DEFAULT_DOCUMENT_NAME);
        assertThat(testDocument.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
    }

    @Test
    void createDocumentWithExistingId() throws Exception {
        // Create the Document with an existing ID
        document.setId(1L);

        int databaseSizeBeforeCreate = documentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(document))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll().collectList().block();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDocuments() {
        // Initialize the database
        documentRepository.save(document).block();

        // Get all the documentList
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
            .value(hasItem(document.getId().intValue()))
            .jsonPath("$.[*].documentName")
            .value(hasItem(DEFAULT_DOCUMENT_NAME))
            .jsonPath("$.[*].employeeId")
            .value(hasItem(DEFAULT_EMPLOYEE_ID.intValue()));
    }

    @Test
    void getDocument() {
        // Initialize the database
        documentRepository.save(document).block();

        // Get the document
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, document.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(document.getId().intValue()))
            .jsonPath("$.documentName")
            .value(is(DEFAULT_DOCUMENT_NAME))
            .jsonPath("$.employeeId")
            .value(is(DEFAULT_EMPLOYEE_ID.intValue()));
    }

    @Test
    void getNonExistingDocument() {
        // Get the document
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewDocument() throws Exception {
        // Initialize the database
        documentRepository.save(document).block();

        int databaseSizeBeforeUpdate = documentRepository.findAll().collectList().block().size();

        // Update the document
        Document updatedDocument = documentRepository.findById(document.getId()).block();
        updatedDocument.documentName(UPDATED_DOCUMENT_NAME).employeeId(UPDATED_EMPLOYEE_ID);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedDocument.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedDocument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll().collectList().block();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testDocument.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
    }

    @Test
    void putNonExistingDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().collectList().block().size();
        document.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, document.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(document))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll().collectList().block();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().collectList().block().size();
        document.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(document))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll().collectList().block();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().collectList().block().size();
        document.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(document))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll().collectList().block();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDocumentWithPatch() throws Exception {
        // Initialize the database
        documentRepository.save(document).block();

        int databaseSizeBeforeUpdate = documentRepository.findAll().collectList().block().size();

        // Update the document using partial update
        Document partialUpdatedDocument = new Document();
        partialUpdatedDocument.setId(document.getId());

        partialUpdatedDocument.documentName(UPDATED_DOCUMENT_NAME).employeeId(UPDATED_EMPLOYEE_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDocument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDocument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll().collectList().block();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testDocument.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
    }

    @Test
    void fullUpdateDocumentWithPatch() throws Exception {
        // Initialize the database
        documentRepository.save(document).block();

        int databaseSizeBeforeUpdate = documentRepository.findAll().collectList().block().size();

        // Update the document using partial update
        Document partialUpdatedDocument = new Document();
        partialUpdatedDocument.setId(document.getId());

        partialUpdatedDocument.documentName(UPDATED_DOCUMENT_NAME).employeeId(UPDATED_EMPLOYEE_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDocument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDocument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll().collectList().block();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testDocument.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
    }

    @Test
    void patchNonExistingDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().collectList().block().size();
        document.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, document.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(document))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll().collectList().block();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().collectList().block().size();
        document.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(document))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll().collectList().block();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().collectList().block().size();
        document.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(document))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll().collectList().block();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDocument() {
        // Initialize the database
        documentRepository.save(document).block();

        int databaseSizeBeforeDelete = documentRepository.findAll().collectList().block().size();

        // Delete the document
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, document.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Document> documentList = documentRepository.findAll().collectList().block();
        assertThat(documentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
