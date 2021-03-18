import Vue from 'vue';

type ToasterType = 'success' | 'info' | 'error';

export class Toaster {

    static error(message: string) {
        Toaster.show(message, 'error');
    }

    static success(message: string) {
        Toaster.show(message, 'success');
    }

    private static show(message: string, type: ToasterType) {
        (Vue as any).toasted.show(message, {
            type: type,
            position: 'bottom-right',
            duration: 5000,
            theme: 'bubble'
        });
    }
}

export default Toaster;
