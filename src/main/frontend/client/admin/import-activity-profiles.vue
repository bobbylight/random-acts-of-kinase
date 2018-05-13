<template>
    <v-container grid-list-md>
        <v-layout row wrap class="import-activity-profiles-wrapper">

            <section-header>Import Activity Profiles</section-header>

            <v-flex xs12 v-if="!file">

                <div>Upload a CSV file with activity profile information to add it to the database.
                The file should contain the following columns, in this order (data can be sparse).
                A header row with values matching these header names is required:</div>

                <div class="headers-example">
                    <img src="img/import-activity-profile-headers.png">
                </div>

                <v-checkbox class="import-activity-profiles-form-field"
                    disabled
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
                    <v-btn color="success" @click="onImport"
                        :disabled="loading">Import</v-btn>
                    <v-btn @click="onCancel"
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
                                <v-select
                                    class="import-filter-select"
                                    :items="[ 'none', 'new', 'modified', 'unmodified' ]"
                                    :disabled="loading"
                                    @change="filterPreviewGridItems($event)"
                                    label="Filter"
                                    single-line
                                    auto
                                    prepend-icon="fa-filter"
                                    hide-details
                                ></v-select>
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
import Toaster from '../toaster';
import { ObjectImportRep, ErrorResponse, FieldStatus } from '../rak';
import { Watch } from 'vue-property-decorator';
import restApi from '../rest-api';
import RakUtil from '../util';
import ImportSummary, { LoadingStatus, PreviewGridFilterType } from './import-summary.vue';

@Component({ components: { SectionHeader, FileDropzone, ImportPreviewTable, ImportSummary } })
export default class ImportActivityProfiles extends Vue {

    private file: File | null = null;

    private importRep: ObjectImportRep | null = null;
    private fileName: string | null = null;
    private headerRow: boolean = true;

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

    private filterPreviewGridItems(type: PreviewGridFilterType) {
        this.previewGridItems = this.createPreviewGridItems(type);
    }

    get previewGridColumnInfos(): ColumnInfo[] {

        return [
            { name: 'Compound', value: 'compoundName' },
            { name: 'Kinase Discoverx', value: 'discoverxGeneSymbol' },
            { name: '% Control', value: 'percentControl' },
            { name: 'Concentration', value: 'compoundConcentration' }
        ];
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
            this.fileName = this.file!.name;
            restApi.importActivityProfiles(this.file, this.headerRow, false)
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

        restApi.importActivityProfiles(this.file!, this.headerRow)
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

.import-activity-profiles-wrapper {

    .import-activity-profiles-form-field {
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

        .import-filter-select {
            padding-top: 0 !important;

            label {
                top: 0;
            }
        }
    }
}
</style>
