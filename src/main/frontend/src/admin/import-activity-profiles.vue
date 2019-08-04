<template>
    <abstract-import-data
        header="Import Activity Profiles"
        :details="details"
        image="img/import-activity-profile-headers.png"
        :importFunction="importFunction"
        :previewGridColumnInfos="previewGridColumnInfos">
    </abstract-import-data>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import AbstractImportData, { ImportFunction } from './abstract-import-data.vue';
import { ColumnInfo } from './import-preview-table.vue';
import restApi from '../rest-api';

@Component({ components: { AbstractImportData } })
export default class ImportActivityProfiles extends Vue {

    private readonly details: string = 'Upload a CSV file with activity profile information to add it to the ' +
        'database. The file should contain the following columns, in this order. If there is a header row, the ' +
        'actual column names in the CSV file do not matter. Data can be sparse:';

    get previewGridColumnInfos(): ColumnInfo[] {

        return [
            { name: 'Compound', value: 'compoundName' },
            { name: 'Kinase Discoverx', value: 'discoverxGeneSymbol' },
            { name: '% Control', value: 'percentControl' },
            { name: 'Concentration', value: 'compoundConcentration' }
        ];
    }

    get importFunction(): ImportFunction {
        return restApi.importActivityProfiles.bind(restApi);
    }
}
</script>
