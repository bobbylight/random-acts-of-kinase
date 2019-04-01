export default class AuditActions {

    private static actions: any = {
        '': '(All)',
        'access_denied': 'Access Denied',
        'login': 'Login',
        'logout': 'Logout',
        'update_compound': 'Update Compound'
    };

    static getLabel(action: string): string {
        return AuditActions.actions[action];
    }

    static getLabelValues(): any {

        const labelValues: any[] = [];

        Object.keys(AuditActions.actions).forEach((key: string) => {
            labelValues.push({ value: key, text: AuditActions.actions[key] });
        });
        return labelValues;
    }
}
