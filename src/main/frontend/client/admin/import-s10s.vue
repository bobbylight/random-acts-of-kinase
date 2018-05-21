<template>
    <abstract-import-data
        header="Import s(10)s"
        :details="details"
        image="img/import-s10-headers.png"
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
export default class ImportS10s extends Vue {

    private details: string = 'Upload a CSV file with S-scores to add it to the database.  Only s(10) records ' +
        'will be imported.' +
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
        return restApi.importS10s.bind(restApi);
    }
}
</script>
