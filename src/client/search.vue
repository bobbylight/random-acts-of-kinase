<template>
    <div>
        <div class="search-filter-wrapper">
            <div class="ui container">

                <h2 class="ui header">Search Compounds</h2>

                <search-filters :filters="filters"></search-filters>
            </div>
        </div>

        <div class="ui container">
            <compound-name-table :filters="gridFilters"></compound-name-table>
        </div>
    </div>
</template>

<script>
import debounce from 'debounce';
import SearchFilters from './search-filters.vue';
import CompoundNameTable from './compound-name-table.vue';

export default {
    components: {
        SearchFilters,
        CompoundNameTable
    },
    data() {
        return {
            filters: {
                inhibitor: '',
                kinase: '',
                activity: ''
            },
            gridFilters: {
                inhibitor: '',
                kinase: '',
                activity: ''
            }
        };
    },
    created() {

        console.log('search.vue created!');
        this.debouncedRefreshTable = debounce(this.refreshTable, 750);
    },
    watch: {
        'filters.inhibitor': function(newFilter) {
            this.debouncedRefreshTable();
        },
        'filters.kinase': function(newFilter) {
            this.debouncedRefreshTable();
        },
        'filters.activity': function(newFilter) {
            this.debouncedRefreshTable();
        }
    },
    methods: {
        refreshTable: function() {
            //this.gridFilters = JSON.parse(JSON.stringify(this.filters));
            this.gridFilters.inhibitor = this.filters.inhibitor;
            this.gridFilters.kinase = this.filters.kinase;
            this.gridFilters.activity = this.filters.activity;
            console.log('New gridFilters: ' + JSON.stringify(this.gridFilters));
        }
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
</style>
