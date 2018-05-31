<template>
    <div class="search-wrapper">

        <v-container fluid grid-list-md class="search-filter-wrapper">
            <v-layout row wrap justify-center>
                <v-flex xs12 class="search-part-layout">

                    <h1>Search Compounds</h1>

                    <search-filters :filters="filters"></search-filters>
                </v-flex>
            </v-layout>
        </v-container>

        <v-container class="search-results">
            <v-layout row wrap justify-center>
                <v-flex xs12 class="search-part-layout">
                    <search-result-table :filters="gridFilters"></search-result-table>
                </v-flex>
            </v-layout>
        </v-container>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Watch } from 'vue-property-decorator';
import debounce from 'debounce';
import SearchFilters from './search-filters.vue';
import SearchResultTable from './search-result-table.vue';

interface Filter {
    inhibitor: string;
    kinase: string;
    activity: any;
}

@Component({ components: { SearchFilters, SearchResultTable } })
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
        this.refreshTable = debounce(this.refreshTable, 750);
    }

    @Watch('filters.inhibitor')
    private onInhibitorFilterChanged(newFilter: string) {
        this.refreshTable();
    }

    @Watch('filters.kinase')
    private onKinaseFilterChanged(newFilter: string) {
        if (!this.filters.activity) {
            console.log('Defaulting activity to 10');
            this.filters.activity = 10;
        }
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
@import "../../styles/app-variables";

.search-wrapper {

    height: 100%;
    background: white;

    .search-part-layout {
        max-width: @max-width;
    }

    .search-filter-wrapper {

        background: #f5f5f5;
        border-bottom: 1px solid lightgray;
        padding: 2rem 6rem;

        .ui.header {
            margin-top: 0;
        }
    }
}
</style>
