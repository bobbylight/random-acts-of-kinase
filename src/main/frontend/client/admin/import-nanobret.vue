<template>
    <abstract-import-data
        header="Import NanoBRET"
        :details="details"
        image="img/import-nanobret-headers.png"
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
export default class ImportNanoBRET extends Vue {

    private details: string = 'Upload a CSV file with NanoBRET data to add it to the database. ' +
        'The file should contain the following columns, in this order. If there is a header row, the ' +
        'actual column names in the CSV file do not matter. Data can be sparse:';

    get previewGridColumnInfos(): ColumnInfo[] {

        return [
            { name: 'Compound Name', value: 'compoundName' },
            { name: 'Chemotype', value: 'chemotype' },
            { name: 's(10)', value: 's10' },
            { name: 'SMILES', value: 'smiles' },
            { name: 'Source', value: 'source' }
        ];
    }

    get importFunction(): ImportFunction {
        return restApi.importNanoBret.bind(restApi);
    }
}
</script>
