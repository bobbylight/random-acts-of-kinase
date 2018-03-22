export interface ActivityProfile {
    id: number;
    compoundName: string;
    kinase: Kinase;
    percentControl: number;
    compoundConcentration: number;
    kd: number;
}

export interface Compound {
    compoundName: string;
    chemotype: string;
    s10: string;
}

export interface Kinase {
    id: number;
    discoverxGeneSymbol: string;
    entrezGeneSymbol: string;
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
}
