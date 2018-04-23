<template>
    <div>
        <div class="file-dropzone">

            <div class="file-dropzone-content" v-if="!file">
                <div class="headline">Drop file here, or</div>
                <label for="compound-file-input">
                    <v-btn @click="onSelectFile" color="success">Select File</v-btn>
                    <input type="file" id="compound-file-input"
                           @change="onFileChanged"
                            ref="fileInput">
                </label>
            </div>


            <div class="file-dropzone-content" v-if="file">
                <div class="headline">File: {{fileName}}</div>

                <div class="import-details">{{importSummary}}</div>

                <div>
                    <v-spacer></v-spacer>
                    <v-btn color="success" @click="onImport">Import</v-btn>
                    <v-btn @click="onCancel">Cancel</v-btn>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import Toaster from './toaster';
import restApi from './rest-api';
import { CompoundImportRep, CompoundStatusPair, ErrorResponse } from './rak';

@Component
export default class ImportCompounds extends Vue {

    private importRep: CompoundImportRep | null = null;
    private file: File | null = null;
    private fileName: string | null = null;

    get importSummary() {

        if (!this.importRep) {
            return 'Error getting import description';
        }

        const csps: CompoundStatusPair[] = this.importRep.compoundStatuses;
        const newCount: number = csps.filter((csp: CompoundStatusPair) => {
            return csp.status === 'NEW_COMPOUND';
        }).length;
        const updateCount: number = csps.length - newCount;

        return `Creating ${newCount} compounds, updating ${updateCount} compounds`;
    }

    private onCancel() {
        this.importRep = this.file = this.fileName = null;
    }

    private onFileChanged() {

        const fileInput: HTMLInputElement = this.$refs.fileInput as HTMLInputElement;
        const file: File = fileInput.files![0];

        if (fileInput) {
            restApi.importCompounds(file, false)
                .then((importRep: CompoundImportRep) => {
                    this.importRep = importRep;
                    this.file = file;
                    this.fileName = this.file.name;
                })
                .catch((errorResponse: ErrorResponse) => {
                    this.importRep = this.file = this.fileName = null;
                    Toaster.error(errorResponse.message);
                });
        }
        else {
            this.file = this.fileName = null;
        }
    }

    private onImport() {

        restApi.importCompounds(this.file!)
            .then(() => {
                Toaster.success('Import successful');
            })
            .catch((errorResponse: ErrorResponse) => {
                Toaster.error(errorResponse.message);
            });
    }

    private onSelectFile() {

        const input: HTMLInputElement = this.$refs.fileInput as HTMLInputElement;
        input.click();
    }
}
</script>

<style lang="less">
.file-dropzone {

    max-width: 800px;
    margin: 0 auto;
    border: 1px solid #e0e0e0;
    border-radius: 3px;
    text-align: center;

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
