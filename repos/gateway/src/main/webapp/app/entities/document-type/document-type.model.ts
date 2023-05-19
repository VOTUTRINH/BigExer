import { IDocument } from 'app/entities/document/document.model';

export interface IDocumentType {
  id?: number;
  description?: string | null;
  documents?: IDocument[] | null;
}

export class DocumentType implements IDocumentType {
  constructor(public id?: number, public description?: string | null, public documents?: IDocument[] | null) {}
}

export function getDocumentTypeIdentifier(documentType: IDocumentType): number | undefined {
  return documentType.id;
}
