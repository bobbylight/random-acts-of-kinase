<template>
    <div class="ui container admin-main">

        <div class="no-access" v-if="!this.$store.getters.loggedIn">

            <h1>You don't have access to this information :(</h1>

            <router-link :to="{ name: 'home' }">Get out of here</router-link>
        </div>

        <div v-if="this.$store.getters.loggedIn">

            <v-card class="top-card-padding">

                <v-card-title primary-title>
                    <div>
                        <h3 class="headline">Incomplete Compounds</h3>
                        <div>Compounds without a SMILES string or s(10)</div>
                    </div>
                </v-card-title>

                <v-card-text>
                    <CompoundsTable
                        url="/admin/api/incompleteCompounds"
                        :columnInfo="incompleteCompoundColumnInfo"></CompoundsTable>
                </v-card-text>

                <v-card-actions class="card-action-buttons">
                    <v-btn flat icon
                            href="/admin/api/incompleteCompounds?format=csv&page=0&size=10000">
                        <v-icon>file-download</v-icon>
                    </v-btn>
                </v-card-actions>
            </v-card>

            <v-card class="top-card-padding">

                <v-card-title primary-title>
                    <div>
                        <h3 class="headline">Compounds Missing Activity Profiles</h3>
                        <div>Compounds missing some activity profiles</div>
                    </div>
                </v-card-title>

                <v-card-text>
                    <CompoundsTable
                        url="/admin/api/compoundsMissingActivityProfiles"
                        :columnInfo="compoundsMissingActivityProfilesColumnInfo"></CompoundsTable>
                </v-card-text>

                <v-card-actions>
                    <v-btn flat icon
                           href="/admin/api/compoundsMissingActivityProfiles?format=csv&page=0&size=10000">
                        <v-icon>file-download</v-icon>
                    </v-btn>
                </v-card-actions>

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
        </div>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import CompoundsTable, { ColumnInfo } from './compounds-table.vue';

@Component({ components: { CompoundsTable } })
export default class AdminHome extends Vue {

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
    margin-top: 2rem;
}
</style>
