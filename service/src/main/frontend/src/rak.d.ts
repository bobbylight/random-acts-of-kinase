export interface ActivityProfile {
    id: number;
    compoundName: string;
    kinase: Kinase;
    percentControl: number;
    compoundConcentration: number;
    kd: number;
}

export interface Audit {
    id: number;
    userName: string;
    action: AuditAction;
    ipAddress: string;
    createDate: string;
    success: boolean;
    details?: string;
}

export type AuditAction = 'LOGIN' | 'LOGOUT';

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
    solubility?: number;
    smiles?: string;
    source?: string;
    primaryReference?: string;
    primaryReferenceUrl?: string;
    hidden?: boolean; // Note this is never sent down, but admins can hide stuff later
}

export interface ErrorResponse {
    statusCode: number;
    message: string;
}

export interface Feedback {
    id?: number;
    email?: string;
    ipAddress?: string;
    title: string;
    body: string;
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
    nanosynGeneSymbol: string;
    discoverxUrl: string;
}

export interface NanoBretSearchFilter {
    inhibitor: string;
    kinase: string;
    ic50: number;
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

export interface Partner {
    id: number;
    name: string;
    url: string;
}

export interface RakState {
    user: string;
    lightboxImage: string | null;
    lightboxTitle: string | undefined;
    filters: SearchFilter;
    nanoBretFilters: NanoBretSearchFilter;
    lastAdminRouteName: string;
}

export interface SearchFilter {
    inhibitor: string;
    kinase: string;
    activity: any;
    kd: any;
    activityOrKd: SearchByKinaseSecondComponent;
}

export type SearchByKinaseSecondComponent = 'kd' | 'percentControl';

export interface SelectItem {
    text: string;
    value: any;
}

export interface UserRep {
    userName?: string;
    lightboxImage?: string;
}

export interface VueDataTableOptions {
    page: number;
    itemsPerPage: number;
    sortBy: string[];
    sortDesc: boolean[];
    groupBy: string[];
    groupDesc: boolean[];
    multiSort: boolean;
    mustSort: boolean;
}
