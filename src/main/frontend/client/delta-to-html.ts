import { DeltaStatic, default as Quill } from 'quill';

/**
 * Converts a Quill <code>DocumentStatic</code> into HTML.
 */
export class DeltaToHtml {

    private quill: Quill;

    convert(delta: DeltaStatic): string {

        if (!this.quill) {
            this.quill = new Quill(DeltaToHtml.createParentElement());
        }

        this.quill.setContents(delta);
        return this.quill.root.innerHTML;
    }

    private static createParentElement(): HTMLDivElement {
        const parentElem: HTMLDivElement = document.createElement('div');
        parentElem.style.display = 'none';
        document.body.appendChild(parentElem);
        return parentElem;
    }
}

const deltaToHtml: DeltaToHtml = new DeltaToHtml();
export default deltaToHtml;
