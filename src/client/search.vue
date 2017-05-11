<template>
    <div>

        <div class="ui grid">

            <div class="eight wide column">
                <search-field type="inhibitor" v-model="filters.inhibitor"
                              place-holder="Inhibitor"></search-field>
            </div>

            <div class="eight wide column">
                <search-field type="kinase" v-model="filters.kinase"
                              place-holder="Kinase"></search-field>
            </div>

            <div class="eight wide column">
                <search-field type="activity" v-model="filters.activity"
                              place-holder="Remaining activity" label="%"></search-field>
            </div>

        </div>

        <result-table :filters="gridFilters"></result-table>
    </div>
</template>

<script>
import debounce from 'debounce';
import SearchField from './search-field.vue';
import ResultTable from './result-table.vue';

export default {
    components: {
        SearchField,
        ResultTable
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
            },
            compounds: []
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
