export interface ActivityProfile {
    id: number;
    compoundName: string;
    kinase: Kinase;
    percentControl: number;
    compoundConcentration: number;
    kd: number;
}

export interface BlogPost {
    id?: number;
    title: string;
    body: string;
    author?: string;
    createDate?: string;
}

export interface Compound {
    compoundName: string;
    chemotype: string;
    s10: string;
    smiles?: string;
    source?: string;
    primaryReference?: string;
    primaryReferenceUrl?: string;
}

export interface ErrorResponse {
    statusCode: number;
    message: string;
}

export interface Feedback {
    id?: number;
    title: string;
    body: string;
    author?: string;
    createDate?: string;
}

export interface FieldStatus {
    fieldName: string;
    oldValue?: any;
    newValue: any;
}

export interface Kinase {
    id: number;
    discoverxGeneSymbol: string;
    entrezGeneSymbol: string;
}

export interface ObjectImportRep {
    fieldStatuses: FieldStatus[][];
}

export interface PagedDataRep<T> {
    data: T[];
    start: number;
    count: number;
    total: number;
}

export interface RakState {
    user: string;
}

export interface UserRep {
    userName?: string;
    lightboxImage?: string;
}
