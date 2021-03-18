<template>
    <abstract-import-data
        header="Import s(10)s"
        :details="details"
        :importFunction="importFunction"
        :importFileColumns="importFileColumns"
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

    private readonly details: string = 'Upload a CSV file with S-scores to add it to the database.  Only s(10) ' +
        'records will be imported.' +
        'The file should contain the following columns, in this order. If there is a header row, the ' +
        'actual column names in the CSV file do not matter. Data can be sparse:';

    importFileColumns: string[] = [ 'Compound Name', 'Selectivity Score Type', '# Hits',
            '# Non-Mutant Hits', 'Screening Concentration', 'Selectivity Score' ];

    get previewGridColumnInfos(): ColumnInfo[] {

        return [
            // { name: 'Compound Name', value: 'compoundName' },
            // { name: 'Selectivity Score Type', value: 'selectivityScoreType' },
            // { name: '# Hits', value: 'numberOfHits' },
            // { name: '# Non-Mutant Hits', value: 'numberOfNonMutantHits' },
            // { name: 'Screening Concentration', value: 'screeningConcentration' },
            // { name: 'Selectivity Score', value: 'selectivityScore' }
            { name: 'Compound Name', value: 'compoundName' },
            { name: 'Chemotype', value: 'chemotype' },
            { name: 's(10)', value: 's10' },
            { name: 'Solubility', value: 'solubility' },
            { name: 'SMILES', value: 'smiles' },
            { name: 'Source', value: 'source' },
            { name: 'Reference', value: 'primaryReference' },
            { name: 'Reference URL', value: 'primaryReferenceUrl' },
            { name: 'Hidden', value: 'hidden' }
        ];
    }

    get importFunction(): ImportFunction {
        return restApi.importS10s.bind(restApi);
    }
}
</script>
