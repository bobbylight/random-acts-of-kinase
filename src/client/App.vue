<template>
    <div>

        <navbar></navbar>

        <div class="ui main text container">

            This is the application body
            <search-field :initial-filter="filter" @change="filterChanged"></search-field>

            <result-table :filter="gridFilter"></result-table>
        </div>
    </div>
</template>

<script type="typescript">

    import debounce from 'debounce';
    import navbar from './navbar.vue';
    import SearchField from './search-field.vue';
    import ResultTable from './result-table.vue';

    export default {
        name: 'app',
        components: {
            SearchField,
            navbar,
            ResultTable
        },
        data() {
            return {
                filter: 'TAK',
                gridFilter: 'TAK',
                compounds: []
            };
        },
        created() {
        	console.log('app created!');
        	this.debouncedRefreshTable = debounce(this.refreshTable, 500);
        },
        methods: {
            filterChanged: function(newValue) {
                console.log('Filter changed to: ' + newValue);
                this.debouncedRefreshTable(newValue);
            },

            refreshTable: function(newValue) {
                this.gridFilter = this.filter = newValue;
            }
        }
    };
</script>

<style scoped>
    .main.container {
        margin-top: 7em;
    }
</style>
