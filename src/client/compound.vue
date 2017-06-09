<template>
    <div class="ui container compound-results">
        <div class="ui header">Results for {{this.id}}</div>
        <result-table :filters="gridFilters"></result-table>
    </div>
</template>

<script>
import ResultTable from './result-table.vue';

export default {
    name: 'compound',
    components: {
        ResultTable
    },
    props: {
        id: { // Comes in from props: true on route
            type: String,
            required: true
        }
    },
    computed: {
        gridFilters: function() {
            return {
                inhibitor: this.id,//this.filters.inhibitor,
                kinase: '',//this.filters.kinase,
                activity: ''//this.filters.activity
            };
        }
    },
    data() {
        return {
        }
    }
}
</script>

<style lang="less">
.compound-results {
    padding-top: 1rem; // Matches search.vue
}
</style>
