import { IDocumentType } from 'app/entities/document-type/document-type.model';

export interface IDocument {
  id?: number;
  documentName?: string | null;
  employeeId?: number | null;
  documentType?: IDocumentType | null;
}

export class Document implements IDocument {
  constructor(
    public id?: number,
    public documentName?: string | null,
    public employeeId?: number | null,
    public documentType?: IDocumentType | null
  ) {}
}

export function getDocumentIdentifier(document: IDocument): number | undefined {
  return document.id;
}
