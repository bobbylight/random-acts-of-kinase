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
                <div class="import-details">{{importSummary}}</div>
            </v-flex>

            <v-flex xs6 v-if="file">
                <v-layout justify-end>
                    <v-btn color="success" @click="onImport"
                        :disabled="loading">Import</v-btn>
                    <v-btn @click="onCancel"
                        :disabled="loading">Cancel</v-btn>
                </v-layout>
            </v-flex>

            <v-flex class="import-activity-profile-table-card" xs12 v-if="file">

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

@Component({ components: { SectionHeader, FileDropzone, ImportPreviewTable } })
export default class ImportActivityProfiles extends Vue {

    private file: File | null = null;

    private importRep: ObjectImportRep | null = null;
    private fileName: string | null = null;
    private headerRow: boolean = true;

    private previewGridItems: any[] = [];
    private loading: boolean = true;

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
            { name: 'Compound', value: 'compoundName' },
            { name: 'Kinase Discoverx', value: 'discoverxGeneSymbol' },
            { name: '% Control', value: 'percentControl' },
            { name: 'Concentration', value: 'compoundConcentration' }
        ];
    }

    get importSummary() {

        if (!this.importRep) {
            return this.loading ? 'Loading, please wait...' :
                'Error getting import description';
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

        this.loading = true;

        if (this.file) {
            this.fileName = this.file!.name;
            restApi.importActivityProfiles(this.file, this.headerRow, false)
                .then((importRep: ObjectImportRep) => {
                    this.importRep = importRep;
                    this.previewGridItems = this.createPreviewGridItems();
                    this.loading = false;
                })
                .catch((errorResponse: ErrorResponse) => {
                    this.reset();
                    this.loading = false;
                    this.previewGridItems = [];
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
    }
}
</style>
