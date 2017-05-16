<template>
    <div>

        <h2 class="ui header">Search Compounds</h2>

        <div>Search by compound:</div>
        <div class="ui grid">
            <div class="sixteen wide column">
                <search-field v-model="filters.inhibitor"
                              place-holder="Inhibitor"></search-field>
            </div>
        </div>

        <div style="margin-top: 1rem">Or by kinase and activity:</div>
        <div class="ui grid">
            <div class="twelve wide column">
                <!--<search-field type="kinase" v-model="filters.kinase"-->
                              <!--place-holder="Kinase"></search-field>-->
                <lazy-dropdown v-model="filters.kinase"
                               name="kinase" url="api/kinases?filter={query}"></lazy-dropdown>
            </div>

            <div class="four wide column">
                <search-field type="number" v-model="filters.activity" numeric="true"
                              place-holder="Remaining activity" label="%"></search-field>
            </div>

        </div>

        <div class="ui divider"></div>

        <compound-name-table :filters="gridFilters"></compound-name-table>
    </div>
</template>

<script>
import debounce from 'debounce';
import SearchField from './search-field.vue';
import LazyDropdown from './lazy-dropdown.vue';
import CompoundNameTable from './compound-name-table.vue';

export default {
    components: {
        SearchField,
        LazyDropdown,
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

        console.log('main.vue created!');
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
</style>
