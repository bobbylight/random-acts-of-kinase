<template>
    <div>
        <div>Showing all results for compound: {{this.id}}</div>
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
