<template>
    <abstract-import-data
        header="Import K<sub>d</sub>s"
        :details="details"
        image="img/import-kd-headers.png"
        :importFunction="importFunction"
        :previewGridColumnInfos="previewGridColumnInfos">
    </abstract-import-data>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import AbstractImportData, { ImportFunction } from './abstract-import-data';
import { ColumnInfo } from './import-preview-table.vue';
import restApi from '../rest-api';

@Component({ components: { AbstractImportData } })
export default class ImportKds extends Vue {

    private details: string = 'Upload a CSV file with K<sub>d</sub> information to add it to the database. ' +
        'The file should contain the following columns, in this order. If there is a header row, the ' +
        'actual column names in the CSV file do not matter. Data can be sparse:';

    get previewGridColumnInfos(): ColumnInfo[] {

        return [
            { name: 'Compound', value: 'compoundName' },
            { name: 'Kinase Discoverx', value: 'discoverxGeneSymbol' },
            { name: 'Kinase Entrez', value: 'entrezGeneSymbol' },
            //{ name: 'Modifier', value: 'modifier' },
            { name: 'K<sub>d</sub> (nM)', value: 'kd' }
        ];
    }

    get importFunction(): ImportFunction {
        return restApi.importKds.bind(restApi);
    }
}
</script>
