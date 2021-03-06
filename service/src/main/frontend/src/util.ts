import { Compound, FieldStatus } from './rak';
import { Route } from 'vue-router';

export const DEFAULT_MOLECULE_SVG: string = 'img/molecule-unknown.svg';

export default class RakUtil {

    static getDisplayDate(iso8601Date: string, includeTime: boolean = false): string {
        const date: Date = new Date(iso8601Date);
        return includeTime ? date.toLocaleString() : date.toLocaleDateString();
    }

    static getCompoundImageUrl(compound?: Compound): string {
        // An SVG was generated for the molecule only if the SMILES string is known
        return compound?.smiles ? `img/smiles/${encodeURIComponent(compound.compoundName)}.svg` :
            DEFAULT_MOLECULE_SVG;
    }

    static isActiveTab($route: Route, tabName: string): boolean {
        const tabNameRegex: RegExp = new RegExp('^' + tabName.replace(/[|\\{}()[\]^$+*?.]/g, '\\$&'));
        console.log(`tabName: ${tabName}, tabNameRegex: ${tabNameRegex}, ` +
            `!!$route.fullPath: ${!!$route.fullPath}, decodedRouteFullPath: ${decodeURIComponent($route.fullPath)}`);
        return !!$route.fullPath && !!decodeURIComponent($route.fullPath).match(tabNameRegex);
    }

    static isNewFieldStatus(fieldStatus: FieldStatus): boolean {
        return (fieldStatus.newValue != null && fieldStatus.oldValue == null) ||
            (fieldStatus.newValue == null && fieldStatus.oldValue == null);
    }

    static isNewRecord(record: FieldStatus[]): boolean {
        for (let i: number = 0; i < record.length; i++) {
            if (!RakUtil.isNewFieldStatus(record[i])) {
                return false;
            }
        }
        return true;
    }

    static isUnchangedFieldStatus(fieldStatus: FieldStatus): boolean {
        return fieldStatus.newValue === fieldStatus.oldValue;
    }

    static isUnchangedRecord(record: FieldStatus[]): boolean {
        for (let i: number = 0; i < record.length; i++) {
            if (!RakUtil.isUnchangedFieldStatus(record[i])) {
                return false;
            }
        }
        return true;
    }

    static programmaticallyClickLink(url: string) {
        const a: HTMLAnchorElement = document.createElement('a');
        a.style.display = 'none';
        a.addEventListener('click', () => {
            window.location.href = url;
        });
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
    }
}
