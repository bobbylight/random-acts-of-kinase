<template>
    <v-container fluid grid-list-md>

        <v-layout row wrap class="search-filter-wrapper">
            <v-flex xs12>

                    <h1>Search Compounds</h1>

                    <search-filters :filters="filters"></search-filters>
            </v-flex>
        </v-layout>

        <v-layout row wrap justify-center class="search-results">
            <v-flex xs10>
                <compound-name-table :filters="gridFilters"></compound-name-table>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Watch } from 'vue-property-decorator';
import debounce from 'debounce';
import SearchFilters from './search-filters.vue';
import CompoundNameTable from './compound-name-table.vue';

interface Filter {
    inhibitor: string;
    kinase: string;
    activity: string;
}

@Component({ components: { SearchFilters, CompoundNameTable } })
export default class Search extends Vue {

    filters: Filter = {
        inhibitor: '',
        kinase: '',
        activity: ''
    };

    gridFilters: Filter = {
        inhibitor: '',
        kinase: '',
        activity: ''
    };

    created() {
        console.log('search.vue created!');
        this.refreshTable = debounce(this.refreshTable, 750);
    }

    @Watch('filters.inhibitor')
    private onInhibitorFilterChanged(newFilter: string) {
        this.refreshTable();
    }

    @Watch('filters.kinase')
    private onKinaseFilterChanged(newFilter: string) {
        this.refreshTable();
    }

    @Watch('filters.activity')
    private onActivityFilterChanged(newFilter: string) {
        this.refreshTable();
    }

    private refreshTable() {
        //this.gridFilters = JSON.parse(JSON.stringify(this.filters));
        this.gridFilters.inhibitor = this.filters.inhibitor;
        this.gridFilters.kinase = this.filters.kinase;
        this.gridFilters.activity = this.filters.activity;
        console.log('New gridFilters: ' + JSON.stringify(this.gridFilters));
    }
}
</script>

<style lang="less">
.search-filter-wrapper {
    background: #f5f5f5;
    border-bottom: 1px solid lightgray;
    padding: 2rem 6rem;

    .ui.header {
        margin-top: 0;
    }
}

.search-results {
    background: white;
}
</style>
