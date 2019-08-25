<template>
    <div>
        <v-data-table
            :headers="headers"
            class="compound-table"
            :items="items"
            :pagination.sync="pagination"
            :total-items="totalItems"
            :loading="loading"
            :items-per-page-options='[ 20, 50, 100 ]'
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
                <td>{{props.item.kinase.nanosynGeneSymbol}}</td>
                <td>{{props.item.kd}}</td>
            </template>
        </v-data-table>
    </div>
</template>

<script lang="ts">
import restApi from './rest-api';
import { PagedDataRep } from '@/rak';

export default {
    name: 'result-table',
    computed: {
        headers: function() {
            return [
                { text: 'Compound', value: 'compoundName' },
                { text: '% Control', value: 'percentControl', align: 'right' },
                { text: 'Concentration (nM)', value: 'compoundConcentration', align: 'right' },
                { text: 'Kinase DiscoverX', value: 'kinase.discoverxGeneSymbol' },
                { text: 'Kinase Entrez', value: 'kinase.entrezGeneSymbol' },
                { text: 'Kinase Nanosyn', value: 'kinase.nanosynGeneSymbol' },
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
            totalItems: 0,
            items: [],
            loading: true,
            pagination: {
                sortBy: 'percentControl'
            }
        };
    },
    watch: {
        'filters.inhibitor': function(newFilter: string) {
            console.log('inhibitor changed');
            this.reloadTable();
        },
        'filters.kinase': function(newFilter: string) {
            console.log('kinase changed');
            this.reloadTable();
        },
        'filters.activity': function(newFilter: string) {
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

        addSuppliedFilters: function(data: any) {
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

        changeSort: function(column: string) {
            if (this.pagination.sortBy === column) {
                this.pagination.descending = !this.pagination.descending;
            } else {
                this.pagination.sortBy = column;
                this.pagination.descending = false;
            }
        },

        getHeaderStyle: function(header: any) {
            return `text-align: ${header.align || 'left'}`;
        },

        reloadTable: function() {

            this.loading = true;

            const { sortBy, descending, page, rowsPerPage }: any = this.pagination;

            const sort: string = sortBy ? `${sortBy},${descending ? 'desc' : 'asc'}` : '';

            return restApi.getActivityProfiles(page - 1, rowsPerPage, this.filters, sort)
                .then((pagedData: PagedDataRep<any>) => {
                    this.items = pagedData.data;
                    this.totalItems = pagedData.total;
                    this.loading = false;
                    return pagedData;
                });
        },

        compoundRenderer: function(data: string, type: string, row: string) {
            return '<a href="#/compound/' + data + '">' + data + '</a>';
        }
    }
};
</script>
