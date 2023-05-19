jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DocumentService } from '../service/document.service';
import { IDocument, Document } from '../document.model';
import { IDocumentType } from 'app/entities/document-type/document-type.model';
import { DocumentTypeService } from 'app/entities/document-type/service/document-type.service';

import { DocumentUpdateComponent } from './document-update.component';

describe('Component Tests', () => {
  describe('Document Management Update Component', () => {
    let comp: DocumentUpdateComponent;
    let fixture: ComponentFixture<DocumentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let documentService: DocumentService;
    let documentTypeService: DocumentTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DocumentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      documentService = TestBed.inject(DocumentService);
      documentTypeService = TestBed.inject(DocumentTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call DocumentType query and add missing value', () => {
        const document: IDocument = { id: 456 };
        const documentType: IDocumentType = { id: 93954 };
        document.documentType = documentType;

        const documentTypeCollection: IDocumentType[] = [{ id: 32246 }];
        jest.spyOn(documentTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: documentTypeCollection })));
        const additionalDocumentTypes = [documentType];
        const expectedCollection: IDocumentType[] = [...additionalDocumentTypes, ...documentTypeCollection];
        jest.spyOn(documentTypeService, 'addDocumentTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ document });
        comp.ngOnInit();

        expect(documentTypeService.query).toHaveBeenCalled();
        expect(documentTypeService.addDocumentTypeToCollectionIfMissing).toHaveBeenCalledWith(
          documentTypeCollection,
          ...additionalDocumentTypes
        );
        expect(comp.documentTypesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const document: IDocument = { id: 456 };
        const documentType: IDocumentType = { id: 34580 };
        document.documentType = documentType;

        activatedRoute.data = of({ document });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(document));
        expect(comp.documentTypesSharedCollection).toContain(documentType);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Document>>();
        const document = { id: 123 };
        jest.spyOn(documentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ document });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: document }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(documentService.update).toHaveBeenCalledWith(document);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Document>>();
        const document = new Document();
        jest.spyOn(documentService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ document });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: document }));
        saveSubject.complete();

        // THEN
        expect(documentService.create).toHaveBeenCalledWith(document);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Document>>();
        const document = { id: 123 };
        jest.spyOn(documentService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ document });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(documentService.update).toHaveBeenCalledWith(document);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackDocumentTypeById', () => {
        it('Should return tracked DocumentType primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDocumentTypeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
