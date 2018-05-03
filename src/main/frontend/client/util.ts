import { FieldStatus } from './rak';

export default class RakUtil {

    static isNewFieldStatus(fieldStatus: FieldStatus): boolean {
        return (fieldStatus.newValue && fieldStatus.oldValue == null) ||
            (fieldStatus.newValue == null && fieldStatus.oldValue == null);
    }

    static isUnchangedFieldStatus(fieldStatus: FieldStatus): boolean {
        return fieldStatus.newValue === fieldStatus.oldValue;
    }

    static isNewRecord(record: FieldStatus[]): boolean {
        for (let i: number = 0; i < record.length; i++) {
            if (!RakUtil.isNewFieldStatus(record[i])) {
                return false;
            }
        }
        return true;
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
