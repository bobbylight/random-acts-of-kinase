import { FieldStatus } from './rak';
import { Route } from 'vue-router';

export default class RakUtil {

    static isActiveTab($route: Route, tabName: string): boolean {
        const tabNameRegex: RegExp = new RegExp(tabName + '(?:/.+)?$');
        console.log(tabName + ' -- ' + $route.fullPath + ', ' + (!!$route.fullPath && !!$route.fullPath.match(tabNameRegex)));
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
}
