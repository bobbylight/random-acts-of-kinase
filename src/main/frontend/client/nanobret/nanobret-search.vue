<template>
    <div class="search-wrapper">

        <v-container fluid grid-list-md class="search-filter-wrapper">
            <v-layout row wrap justify-center>
                <v-flex xs12 class="search-part-layout">

                    <h1>Search NanoBRET Data (DOESN'T WORK YET)</h1>

                    <nano-bret-search-filters></nano-bret-search-filters>
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
import NanoBretSearchFilters from './nanobret-search-filters.vue';
import SearchResultTable from '../search/search-result-table.vue';
import { NanoBretSearchFilter } from '../rak';

@Component({ components: { NanoBretSearchFilters, SearchResultTable } })
export default class NanoBretSearch extends Vue {

    gridFilters: NanoBretSearchFilter = {
        inhibitor: '',
        kinase: '',
        ic50: 1000
    };

    created() {
        this.refreshTable = debounce(this.refreshTable, 750);
    }

    get nanoBretFilters(): NanoBretSearchFilter {
        return this.$store.state.nanoBretFilters;
    }

    @Watch('nanoBretFilters', { deep: true })
    private onFiltersChanged() {
        this.refreshTable();
    }

    private refreshTable() {
        //this.gridFilters = JSON.parse(JSON.stringify(this.filters));
        this.gridFilters.inhibitor = this.nanoBretFilters.inhibitor;
        this.gridFilters.kinase = this.nanoBretFilters.kinase;
        this.gridFilters.ic50 = this.nanoBretFilters.ic50;
        console.log('New gridFilters: ' + JSON.stringify(this.gridFilters));
    }
}
</script>
