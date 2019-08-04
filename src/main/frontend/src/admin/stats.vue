<template>
    <v-container grid-list-md>
        <v-layout row wrap class="stats-wrapper">

            <section-header>Missing Data</section-header>

            <v-flex xs12>
                <p>
                    The following reports show areas where our data is incomplete.
                    Each table can be exported in CSV format.
                </p>
            </v-flex>

            <stats-compound-table
                title="Incomplete Compounds"
                subtitle="Compounds without a SMILES string or s(10)"
                table-url="/admin/api/stats/incompleteCompounds"
                download-file-name="incomplete-compounds.csv"
                :table-column-info="incompleteCompoundColumnInfo">
            </stats-compound-table>

            <stats-compound-table
                title="Compounds Missing Activity Profiles"
                subtitle="Compounds missing some activity profiles"
                table-url="/admin/api/stats/compoundsMissingActivityProfiles"
                download-file-name="compounds-missing-activity-profiles.csv"
                :table-column-info="compoundsMissingActivityProfilesColumnInfo">
            </stats-compound-table>

            <stats-compound-table
                title="Compounds Missing Publication Info"
                subtitle="Compounds missing either a publication name or URL"
                table-url="/admin/api/stats/compoundsMissingPublicationInfo"
                download-file-name="compounds-missing-publication-info.csv"
                :table-column-info="compoundsMissingPublicationInfoColumnInfo">
            </stats-compound-table>

            <stats-compound-table
                title="Hidden Compounds"
                subtitle="Compounds hidden in the main application"
                table-url="/admin/api/stats/hiddenCompounds"
                download-file-name="hidden-compounds.csv"
                :table-column-info="hiddenCompoundsColumnInfo">
            </stats-compound-table>

        </v-layout>
    </v-container>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import StatsCompoundTable from './stats-compound-table.vue';
import { ColumnInfo } from './compounds-table.vue';
import SectionHeader from '../header.vue';

@Component({ components: { StatsCompoundTable, SectionHeader } })
export default class Stats extends Vue {

    private incompleteCompoundColumnInfo: ColumnInfo[];
    private compoundsMissingActivityProfilesColumnInfo: ColumnInfo[];
    private compoundsMissingPublicationInfoColumnInfo: ColumnInfo[];
    private hiddenCompoundsColumnInfo: ColumnInfo[];

    created() {

        this.incompleteCompoundColumnInfo = [
            { columnId: 'compoundName', columnName: 'Compound', isCompound: true },
            { columnId: 'chemotype', columnName: 'Chemotype' },
            { columnId: 's10', columnName: 's(10)' },
            { columnId: 'smiles', columnName: 'SMILES' },
            { columnId: 'source', columnName: 'Source' },
            { columnId: 'primaryReference', columnName: 'Reference' },
            { columnId: 'primaryReferenceUrl', columnName: 'Reference URL' },
            { columnId: 'solubility', columnName: 'Solubility' },
            { columnId: 'hidden', columnName: 'Hidden' },
        ];

        this.compoundsMissingActivityProfilesColumnInfo = [
            { columnId: 'compoundName', columnName: 'Compound', isCompound: true },
            { columnId: 'count', columnName: 'Activity Profile Count' }
        ];

        this.compoundsMissingPublicationInfoColumnInfo = this.incompleteCompoundColumnInfo;
        this.hiddenCompoundsColumnInfo = this.incompleteCompoundColumnInfo;
    }
}
</script>
