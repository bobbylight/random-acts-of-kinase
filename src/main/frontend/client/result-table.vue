<template>
    <div>
        <v-data-table
            :headers="headers"
            class="compound-table elevation-1"
            :items="items"
            :search="search"
            :pagination.sync="pagination"
            :total-items="totalItems"
            :loading="loading"
            :rows-per-page-items='[ 20, 50, 100 ]'
        >

            <template slot="items" slot-scope="props">
                <td>{{props.item.compoundName}}</td>
                <td class="text-xs-right">{{props.item.percentControl}}</td>
                <td class="text-xs-right">{{props.item.compoundConcentration}}</td>
                <td>{{props.item.kinase.discoverxGeneSymbol}}</td>
                <td>{{props.item.kinase.entrezGeneSymbol}}</td>
                <td>{{props.item.kd}}</td>
            </template>
        </v-data-table>
    </div>
</template>

<script>
import restApi from 'rest-api';

export default {
    name: 'result-table',
    computed: {
        headers: function() {
            return [
                { text: 'Compound', value: 'compoundName' },
                { text: '% Control', value: 'percentControl', align: 'right' },
                { text: 'Concentration (nM)', value: 'compoundConcentration', align: 'right' },
                { text: 'DiscX Gene Symbol', value: 'discoverxGeneSymbol' },
                { text: 'Entrez Gene Symbol', value: 'entrezGeneSymbol' },
                { text: 'K<sub>d</sub>', value: 'kd' }
            ];
        }
    },
    props: {
        //inhibitor: String,
        filters: {
            type: Object,
            required: true
        }
    },
    data: function() {
        return {
            search: '',
            totalItems: 0,
            items: [],
            loading: true,
            pagination: {}
        };
    },
    watch: {
        'filters.inhibitor': function(newFilter) {
            console.log('inhibitor changed');
            this.reloadTable();
        },
        'filters.kinase': function(newFilter) {
            console.log('kinase changed');
            this.reloadTable();
        },
        'filters.activity': function(newFilter) {
            console.log('activity changed');
            this.reloadTable();
        },

        pagination: {
            handler () {
                this.reloadTable();
            },
            deep: true
        }
    },
    methods: {

        addSuppliedFilters: function(data) {
            if (this.filters.inhibitor) {
                data.compound = this.filters.inhibitor;
            }
            if (this.filters.activity) {
                data.activity = this.filters.activity;
            }
            if (this.filters.kinase) {
                data.kinase = this.filters.kinase;
            }
        },

        reloadTable: function() {

            this.loading = true;

            const { sortBy, descending, page, rowsPerPage } = this.pagination;

            const sort = sortBy ? `${sortBy},${descending ? 'desc' : 'asc'}` : '';

            return restApi.getActivityProfiles(page - 1, rowsPerPage, this.filters, sort)
                .then(pagedData => {
                    this.items = pagedData.data;
                    this.totalItems = pagedData.total;
                    this.loading = false;
                    return pagedData;
                });
        },

        compoundRenderer: function(data, type, row) {
            return '<a href="#/compound/' + data + '">' + data + '</a>';
//                return '<a v-link="{ path: \'/compound/' + data + '\' }">' + data + '</a>';
        }
    }
};
</script>
