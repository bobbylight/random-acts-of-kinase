<template>
    <div>
        <v-data-table
            :headers="headers"
            class="compound-table"
            :items="items"
            :search="search"
            :pagination.sync="pagination"
            :total-items="totalItems"
            :loading="loading"
            :rows-per-page-items='[ 20, 50, 100 ]'
        >

            <!-- Custom header rendering is solely to allow HTML in header.text! -->
            <template slot="headers" slot-scope="props">
                <th
                    v-for="header in props.headers"
                    :key="header.text"
                    :class="['column sortable', pagination.descending ? 'desc' : 'asc', header.value === pagination.sortBy ? 'active' : '']"
                    :style="getHeaderStyle(header)"
                    @click="changeSort(header.value)"
                >
                    <span v-html="header.text"></span>
                    <v-icon small>arrow_upward</v-icon>
                </th>
            </template>

            <template slot="items" slot-scope="props">
                <td>{{props.item.compoundName}}</td>
                <td class="text-xs-right">{{props.item.percentControl}}</td>
                <td class="text-xs-right">{{props.item.compoundConcentration}}</td>
                <td>
                    <a :href="props.item.kinase.discoverxUrl"
                       target="_blank" rel="noopener noreferrer">
                        {{props.item.kinase.discoverxGeneSymbol}}</a>
                </td>
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
                { text: 'DiscX Gene Symbol', value: 'kinase.discoverxGeneSymbol' },
                { text: 'Entrez Gene Symbol', value: 'kinase.entrezGeneSymbol' },
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
            pagination: {
                sortBy: 'percentControl'
            }
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

        changeSort: function(column) {
            if (this.pagination.sortBy === column) {
                this.pagination.descending = !this.pagination.descending;
            } else {
                this.pagination.sortBy = column;
                this.pagination.descending = false;
            }
        },

        getHeaderStyle: function(header) {
            return `text-align: ${header.align || 'left'}`;
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
        }
    }
};
</script>
