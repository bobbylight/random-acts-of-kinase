<template>
    <div>
        <div class="file-dropzone">

            <div class="file-dropzone-content" @drop.prevent="onDrop" @dragover.prevent="onDragOver">
                <div class="headline">Drop file here, or</div>
                <label for="compound-file-input">
                    <v-btn @click="onSelectFileButtonClicked" color="success">Select File</v-btn>
                    <input type="file" id="compound-file-input"
                           @change="onFileChanged"
                           ref="fileInput">
                </label>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';
import Toaster from './toaster';

@Component
export default class ImportCompounds extends Vue {

    /**
     * "value" facilitates v-model support
     */
    @Prop({ required: true })
    value: File | null;

    onDragOver(e: DragEvent) {
        e.dataTransfer.dropEffect = 'copy';
    }

    onDrop(e: DragEvent) {

        const files: FileList = e.dataTransfer.files;
        if (files.length === 0) {
            Toaster.error('The dropped item is not a file');
        }
        else if (files.length > 1) {
            Toaster.error('Only one file can be imported at a time');
        }
        else {
            this.$emit('input', files[0]);
        }
    }

    private onFileChanged() {

        const fileInput: HTMLInputElement = this.$refs.fileInput as HTMLInputElement;
        const file: File = fileInput.files![0];

        this.$emit('input', file);
    }

    private onSelectFileButtonClicked() {
        const input: HTMLInputElement = this.$refs.fileInput as HTMLInputElement;
        input.click();
    }
}
</script>

<style lang="less">
@import '../styles/app';

.file-dropzone {

    .import-form-field();
    border: 1px solid #e0e0e0;
    border-radius: 3px;
    text-align: center;
    transition: background @transition-time;

    &:hover {
        background: #f0f0f0;
    }

    .file-dropzone-content {
        padding: 3rem;

        div.headline {
            padding-bottom: 1rem;
        }
        div.import-details {
            padding-bottom: 1rem;
        }

        #compound-file-input {
            display: none;
        }
    }
}
</style>
