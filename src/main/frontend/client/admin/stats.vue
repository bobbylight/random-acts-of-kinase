<template>
    <v-container grid-list-md>
        <v-card class="top-card-padding">

            <v-card-title primary-title>
                <div class="title-content">
                    <div>
                        <h3 class="headline">Incomplete Compounds</h3>
                        <div>Compounds without a SMILES string or s(10)</div>
                    </div>

                    <div class="admin-card-button-area">
                        <download-button url="/admin/api/incompleteCompounds"></download-button>
                    </div>
                </div>
            </v-card-title>

            <v-card-text>
                <CompoundsTable
                    url="/admin/api/incompleteCompounds"
                    :columnInfo="incompleteCompoundColumnInfo"></CompoundsTable>
            </v-card-text>
        </v-card>

        <v-card class="top-card-padding">

            <v-card-title primary-title>
                <div class="title-content">
                    <div>
                        <h3 class="headline">Compounds Missing Activity Profiles</h3>
                        <div>Compounds missing some activity profiles</div>
                    </div>

                    <div class="admin-card-button-area">
                        <download-button url="/admin/api/compoundsMissingActivityProfiles"></download-button>
                    </div>
                </div>
            </v-card-title>

            <v-card-text>
                <CompoundsTable
                    url="/admin/api/compoundsMissingActivityProfiles"
                    :columnInfo="compoundsMissingActivityProfilesColumnInfo"></CompoundsTable>
            </v-card-text>
        </v-card>

        <v-card class="top-card-padding">

            <v-card-title primary-title>
                <div>
                    <h3 class="headline">Something Else</h3>
                    <div>Some other administrative stuff</div>
                </div>
            </v-card-title>

            <v-card-text>
                Nothing here yet.
            </v-card-text>
        </v-card>
    </v-container>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import CompoundsTable, { ColumnInfo } from './compounds-table.vue';
import DownloadButton from './download-button.vue';

@Component({ components: { CompoundsTable, DownloadButton } })
export default class Stats extends Vue {

    private incompleteCompoundColumnInfo: ColumnInfo[];
    private compoundsMissingActivityProfilesColumnInfo: ColumnInfo[];

    created() {

        this.incompleteCompoundColumnInfo = [
            { columnId: 'compoundName', columnName: 'Compound', isCompound: true },
            { columnId: 'chemotype', columnName: 'Chemotype' },
            { columnId: 's10', columnName: 's(10)' },
            { columnId: 'smiles', columnName: 'SMILES' },
            { columnId: 'source', columnName: 'Source' }
        ];

        this.compoundsMissingActivityProfilesColumnInfo = [
            { columnId: 'compoundName', columnName: 'Compound', isCompound: true },
            { columnId: 'count', columnName: 'Activity Profile Count' }
        ];
    }
}
</script>

<style lang="less">
.top-card-padding {
    /*margin-top: 2rem;*/
}
.title-content {
    position: relative;
    width: 100%;

    .admin-card-button-area {
        position: absolute;
        top: 0;
        right: 0;
    }
}
</style>
