<template>
    <div class="editor"></div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop, Watch } from 'vue-property-decorator';
import Quill, { DeltaStatic, Sources } from 'quill';
import debounce from 'debounce';

/**
 * Event fired when the contents of the editor changes.
 */
export interface ChangeEvent {
    isEmpty(): boolean;
    getContent(): any;
}

@Component
export default class RichTextEditor extends Vue {

    @Prop()
    private initialContent: DeltaStatic | null;

    // Note this class assumes this prop won't change for the life of this component.
    @Prop()
    private emitChangeEvents: boolean | undefined;

    @Prop({ default: '300px' })
    private height: string;

    private editor: Quill;

    private skipNextContentChange: boolean = false;

    created() {
        this.getEditorContent = this.getEditorContent.bind(this);
        this.isEditorEmpty = this.isEditorEmpty.bind(this);
    }

    private getEditorContent(): any {
        return this.editor.getContents();
    }

    private isEditorEmpty(): boolean {
        // Quill provides no easy means of determining if it's actually empty, or just contains an image.
        // Here we're saying that an editor with just an image in it is effectively empty.
        return this.editor.getLength() === 0 ||
            this.editor.getText().trim().length === 0;
    }

    private onTextChange(delta: DeltaStatic, oldContents: DeltaStatic, source: Sources) {
        this.$emit('change', {
            isEmpty: this.isEditorEmpty,
            getContent: this.getEditorContent
        });
    }

    mounted() {

        const toolbarOptions: any[] = [
            [ 'bold', 'italic', 'underline' ],
            [ { color: [] }, { background: [] } ], // Empty arrays => takes defaults from theme
            [ 'link', 'image' ],
            [ { list: 'ordered' }, { list: 'bullet' } ],
            [ { align: [] } ],
            [ { size: [ 'small', false, 'large', 'huge' ] } ],
            [ { header: [ 1, 2, 3, 4, 5, 6, false ] } ],
            [ { font: [] } ],
            [ 'clean' ], // Remove formatting
        ];

        this.$el.style.height = this.height;

        this.editor = new Quill(this.$el, {
            modules: { toolbar: toolbarOptions },
            theme: 'snow',
            placeholder: 'Enter your post here!'
        });

        if (this.emitChangeEvents) {
            this.editor.on('text-change', debounce(this.onTextChange.bind(this), 500));
        }
    }

    @Watch('initialContent')
    private onInitialContentChanged() {
        if (this.initialContent) {
            this.editor.setContents(this.initialContent);
        }
    }
}
</script>

<style lang="less">
</style>
