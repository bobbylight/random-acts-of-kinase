<template>
    <v-container grid-list-md>
        <v-layout row wrap class="import-wrapper">

            <section-header><span v-html="header"></span></section-header>

            <v-flex xs12 v-if="!file">

                <div v-html="details"></div>

                <expected-column-listing  :column-names="expectedColumnNames"/>

                <v-checkbox class="import-form-field"
                            label="The CSV file contains a header row"
                            v-model="headerRow"
                ></v-checkbox>

                <file-dropzone v-model="file"></file-dropzone>
            </v-flex>

            <v-flex xs6 v-if="file">
                <div class="headline">File: {{fileName}}</div>
                <div class="import-details">
                    <import-summary
                        :newCount="newRecordCount"
                        :modifiedCount="modifiedRecordCount"
                        :totalCount="totalRecordCount"
                        :status="loadingStatus"></import-summary>
                </div>
            </v-flex>

            <v-flex xs6 v-if="file">
                <v-layout justify-end>
                    <v-btn class="import-data-button" color="success" @click="onImport"
                           :disabled="loading">Import</v-btn>
                    <v-btn class="import-data-button" @click="onCancel"
                           :disabled="loading">Cancel</v-btn>
                </v-layout>
            </v-flex>

            <v-flex class="import-preview-table-card" xs12 v-if="file">

                <v-card>

                    <v-card-title primary-title>

                        <v-layout class="title-content" row wrap>

                            <v-flex xs8>
                                <h3 class="headline" style="display: inline-block">
                                    Imported Data Preview
                                </h3>
                            </v-flex>
                            <v-flex xs4>
                                <import-preview-table-filter
                                    :disabled="loading"
                                    @change="filterPreviewGridItems($event)"
                                ></import-preview-table-filter>
                            </v-flex>
                        </v-layout>
                    </v-card-title>

                    <import-preview-table
                        :column-info="previewGridColumnInfos"
                        :items="previewGridItems"
                        :loading="loading">
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
import ImportPreviewTableFilter from './import-preview-table-filter.vue';
import Toaster from '../toaster';
import { ObjectImportRep, ErrorResponse, FieldStatus } from '../rak';
import { Prop, Watch } from 'vue-property-decorator';
import RakUtil from '../util';
import ImportSummary, { LoadingStatus, PreviewGridFilterType } from './import-summary.vue';
import ExpectedColumnListing from './expected-column-listing.vue';

/**
 * This just so happens to align with how the service is configured.
 */
const MAX_UPLOAD_FILE_SIZE_IN_MB: number = 11;

/**
 * A function that either imports data, or returns a preview of what an import operation would do.
 * Implementations of this function are found in <code>rest-api.ts</code>.
 */
export interface ImportFunction {
    (file: File, headerRow: boolean, commit?: boolean): Promise<ObjectImportRep>;
}

@Component({ components: { ExpectedColumnListing, SectionHeader, FileDropzone, ImportPreviewTable,
        ImportPreviewTableFilter, ImportSummary } })
export default class AbstractImportData extends Vue {

    @Prop({ required: true })
    private readonly header: string;

    @Prop({ required: true })
    private readonly details: string;

    @Prop()
    private readonly importFileColumns: string[] | undefined;

    @Prop({ required: true })
    private readonly previewGridColumnInfos: ColumnInfo[];

    @Prop({ required: true })
    private readonly importFunction: ImportFunction;

    private file: File | null = null;

    private importRep: ObjectImportRep | null = null;
    private fileName: string | null = null;
    headerRow: boolean = true;

    private previewGridItems: any[] = [];
    private loading: boolean = true;
    private newRecordCount: number = 0;
    private modifiedRecordCount: number = 0;
    private totalRecordCount: number = 0;
    private loadingStatus: LoadingStatus = 'loading';

    private createPreviewGridItems(filterType: PreviewGridFilterType = 'none'): any[] {

        const items: any[] = [];

        this.importRep!.fieldStatuses.forEach((rowData: FieldStatus[]) => {

            if (filterType === 'new' && !RakUtil.isNewRecord(rowData)) {
                return;
            }
            if (filterType === 'modified' && (RakUtil.isNewRecord(rowData) ||
                RakUtil.isUnchangedRecord(rowData))) {
                return;
            }
            if (filterType === 'unmodified' && !RakUtil.isUnchangedRecord(rowData)) {
                return;
            }

            const row: any = {};

            rowData.forEach((fieldStatus: FieldStatus) => {
                row[fieldStatus.fieldName] = fieldStatus;
            });

            items.push(row);
        });

        return items;
    }

    private get expectedColumnNames(): string[] {
        return this.importFileColumns ? this.importFileColumns :
            this.previewGridColumnInfos.map((ci: ColumnInfo) => ci.name);
    }

    private static isFileTooLargeToUpload(file: File): boolean {
        return file.size / 1024 / 1024 > MAX_UPLOAD_FILE_SIZE_IN_MB;
    }

    private filterPreviewGridItems(type: PreviewGridFilterType) {
        this.previewGridItems = this.createPreviewGridItems(type);
    }

    private updateImportSummary() {

        if (!this.importRep) {
            this.loadingStatus = this.loading ? 'loading' : 'error';
            this.newRecordCount = this.modifiedRecordCount = this.totalRecordCount = -1;
            return;
        }

        this.loadingStatus = 'loaded';
        const fieldStatuses: FieldStatus[][] = this.importRep.fieldStatuses;

        this.newRecordCount = fieldStatuses.filter((rowData: FieldStatus[]) => {
            return RakUtil.isNewRecord(rowData);
        }).length;
        const unmodifiedCount: number = fieldStatuses.filter((rowData: FieldStatus[]) => {
            return RakUtil.isUnchangedRecord(rowData);
        }).length;
        this.modifiedRecordCount = fieldStatuses.length - this.newRecordCount - unmodifiedCount;
        this.totalRecordCount = fieldStatuses.length;
    }

    private onCancel() {
        this.reset();
    }

    @Watch('file')
    private onFileChanged() {

        this.loading = true;
        this.previewGridItems = [];
        this.updateImportSummary();

        if (this.file) {

            if (AbstractImportData.isFileTooLargeToUpload(this.file)) {
                this.reset();
                this.loading = false;
                this.previewGridItems = [];
                this.updateImportSummary();
                Toaster.error(`You can't upload files larger than ${MAX_UPLOAD_FILE_SIZE_IN_MB} MB`);
                this.file = null;
                return;
            }

            this.fileName = this.file.name;

            this.importFunction(this.file, this.headerRow, false)
                .then((importRep: ObjectImportRep) => {
                    this.importRep = importRep;
                    this.updateImportSummary();
                    this.previewGridItems = this.createPreviewGridItems();
                    this.loading = false;
                })
                .catch((errorResponse: ErrorResponse) => {
                    this.reset();
                    this.loading = false;
                    this.previewGridItems = [];
                    this.updateImportSummary();
                    Toaster.error(errorResponse.message);
                });
        }
    }

    private onImport() {

        this.loading = true;

        this.importFunction(this.file!, this.headerRow)
            .then(() => {
                this.reset();
                this.loading = false;
                Toaster.success('Import successful');
            })
            .catch((errorResponse: ErrorResponse) => {
                this.loading = false;
                Toaster.error(errorResponse.message);
            });
    }

    private reset() {
        this.importRep = this.file = this.fileName = null;
    }
}
</script>

<style lang="less">
@import '../../styles/app';

.import-wrapper {

    .import-data-button {
        margin-left: 0.5rem;
        margin-right: 0.5rem;
    }

    .import-form-field {
        .import-form-field();
    }

    .column-listing {
        padding: 1rem 0;
    }

    .import-preview-table-card {
        margin-top: 1rem;

        .title-content {
            width: 100%;
        }
    }
}
</style>
