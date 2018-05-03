<template>
    <v-container grid-list-md>
        <v-layout row wrap class="import-activity-profiles-wrapper">

            <section-header>Import Activity Profiles</section-header>

            <v-flex xs12 v-if="!file">

                <div>Upload a CSV file with activity profile information to add
                add to the database. The file should contain the following
                columns (data can be sparse):</div>

                <ul class="column-listing">
                    <li>AAA</li>
                    <li>BBB</li>
                    <li>CCC</li>
                </ul>

                <v-checkbox class="import-activity-profiles-form-field"
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
                    <v-btn color="success" @click="onImport">Import</v-btn>
                    <v-btn @click="onCancel">Cancel</v-btn>
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
export default class ImportActivityprofiles extends Vue {

    private file: File | null = null;

    private importRep: ObjectImportRep | null = null;
    private fileName: string | null = null;
    private headerRow: boolean = true;

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
            { name: 'Compound', value: 'compoundName' },
            { name: 'Kinase Discoverx', value: 'discoverxGeneSymbol' },
            { name: '% Control', value: 'percentControl' },
            { name: 'Concentration', value: 'compoundConcentration' },
            { name: 'Kd', value: 'kd' }
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
            restApi.importActivityProfiles(this.file, this.headerRow, false)
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

        restApi.importActivityProfiles(this.file!, this.headerRow)
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
