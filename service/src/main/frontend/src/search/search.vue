<template>
    <div class="search-wrapper">

        <v-container fluid grid-list-md class="search-filter-wrapper">
            <v-row class="justify-center">
                <v-col cols="12" class="search-part-layout">

                    <section-header style="padding: unset"> Search Compounds</section-header>

                    <search-filters></search-filters>
                </v-col>
            </v-row>
        </v-container>

        <v-container class="search-results">
            <v-row class="justify-center">
                <v-col cols="12" class="search-part-layout">
                    <search-result-table :filters="gridFilters"></search-result-table>
                </v-col>
            </v-row>
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
import { SearchFilter } from '../rak';
import SectionHeader from '../header.vue';

@Component({ components: { SearchFilters, SearchResultTable, SectionHeader } })
export default class Search extends Vue {

    gridFilters: SearchFilter = {
        inhibitor: '',
        kinase: '',
        activity: '',
        kd: '',
        activityOrKd: 'percentControl'
    };

    created() {
        this.refreshTable = debounce(this.refreshTable, 750);
    }

    get filters() {
        return this.$store.state.filters;
    }

    @Watch('filters', { deep: true })
    private onFiltersChanged(newFilter: string) {
        // We don't give these values defaults initially so they only show up if a kinase is selected
        if (this.filters.activityOrKd === 'percentControl' && !this.filters.activity) {
            console.log('Defaulting activity to 10');
            this.filters.activity = 10;
        }
        else if (this.filters.activityOrKd === 'kd' && !this.filters.kd) {
            console.log('Defaulting Kd to 500');
            this.filters.kd = 500;
        }
        this.refreshTable();
    }

    private refreshTable() {
        //this.gridFilters = JSON.parse(JSON.stringify(this.filters));
        this.gridFilters.inhibitor = this.filters.inhibitor;
        this.gridFilters.kinase = this.filters.kinase;
        this.gridFilters.activity = this.filters.activity;
        this.gridFilters.kd = this.filters.kd;
        this.gridFilters.activityOrKd = this.filters.activityOrKd;
        console.log('New gridFilters: ' + JSON.stringify(this.gridFilters));
    }
}
</script>
