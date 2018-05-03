<template>
    <v-container grid-list-md>
        <v-layout row wrap class="import-compounds-wrapper">

            <section-header>Import Compounds</section-header>

            <v-flex xs12 v-if="!file">

                <div>Upload a CSV file with compound information to add it
                to the database. The file should contain the following columns
                (data can be sparse):</div>

                <ul class="column-listing">
                    <li>Compound Name</li>
                    <li>Chemotype</li>
                    <li>s(10)</li>
                    <li>SMILES String</li>
                    <li>Source ("hidden" to hide in the UI)</li>
                </ul>

                <file-dropzone v-model="file"></file-dropzone>
            </v-flex>

            <v-flex xs6 v-if="file">
                <div class="headline">File: {{fileName}}</div>
                <div class="import-details">{{importSummary}}</div>
            </v-flex>

            <v-flex xs6 v-if="file">
                <v-layout justify-end>
                    <v-btn color="success" @click="onImport">Import</v-btn>
                    <v-btn @click="onCancel">Cancel</v-btn>
                </v-layout>
            </v-flex>

            <v-flex class="import-preview-table-card" xs12 v-if="file">

                <v-card>

                    <v-card-title primary-title>

                        <div class="title-content">
                            <h3 class="headline">
                                Imported Data Preview
                            </h3>
                        </div>
                    </v-card-title>

                    <import-preview-table
                        :column-info="previewGridColumnInfos"
                        :items="previewGridItems">
                    </import-preview-table>
                </v-card>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import SectionHeader from '../header.vue';
import FileDropzone from '../file-dropzone.vue';
import ImportPreviewTable, { ColumnInfo } from './import-preview-table.vue';
import Toaster from '../toaster';
import { ObjectImportRep, ErrorResponse, FieldStatus } from '../rak';
import { Watch } from 'vue-property-decorator';
import restApi from '../rest-api';
import RakUtil from '../util';

@Component({ components: { SectionHeader, FileDropzone, ImportPreviewTable } })
export default class ImportCompounds extends Vue {

    private file: File | null = null;

    private importRep: ObjectImportRep | null = null;
    private fileName: string | null = null;

    private previewGridItems: any[] = [];

    private createPreviewGridItems(): any[] {

        const items: any[] = [];

        this.importRep!.fieldStatuses.forEach((rowData: FieldStatus[]) => {

            const row: any = {};

            rowData.forEach((fieldStatus: FieldStatus) => {
                row[fieldStatus.fieldName] = fieldStatus;
            });

            items.push(row);
        });

        return items;
    }

    get previewGridColumnInfos(): ColumnInfo[] {

        return [
            { name: 'Compound Name', value: 'compoundName' },
            { name: 'Chemotype', value: 'chemotype' },
            { name: 's(10)', value: 's10' },
            { name: 'SMILES', value: 'smiles' },
            { name: 'Source', value: 'source' }
        ];
    }

    get importSummary() {

        if (!this.importRep) {
            return 'Error getting import description';
        }

        const fieldStatuses: FieldStatus[][] = this.importRep.fieldStatuses;

        const newRecordCount: number = fieldStatuses.filter((rowData: FieldStatus[]) => {
            return RakUtil.isNewRecord(rowData);
        }).length;
        const unchangedRecordCount: number = fieldStatuses.filter((rowData: FieldStatus[]) => {
            return RakUtil.isUnchangedRecord(rowData);
        }).length;
        const modifiedRecordCount: number = fieldStatuses.length - newRecordCount - unchangedRecordCount;
        return `Creating ${newRecordCount} new records, modifying ${modifiedRecordCount}`;
    }

    private onCancel() {
        this.reset();
    }

    @Watch('file')
    private onFileChanged() {

        if (this.file) {
            restApi.importCompounds(this.file, false)
                .then((importRep: ObjectImportRep) => {
                    this.importRep = importRep;
                    this.previewGridItems = this.createPreviewGridItems();
                    this.fileName = this.file!.name;
                })
                .catch((errorResponse: ErrorResponse) => {
                    this.reset();
                    this.previewGridItems = [];
                    Toaster.error(errorResponse.message);
                });
        }
    }

    private onImport() {

        restApi.importCompounds(this.file!)
            .then(() => {
                this.reset();
                Toaster.success('Import successful');
            })
            .catch((errorResponse: ErrorResponse) => {
                Toaster.error(errorResponse.message);
            });
    }

    private reset() {
        this.importRep = this.file = this.fileName = null;
    }
}
</script>

<style lang="less">
.import-compounds-wrapper {

    .column-listing {
        padding: 1rem 0;
    }

    .import-preview-table-card {
        margin-top: 1rem;
    }
}
</style>
