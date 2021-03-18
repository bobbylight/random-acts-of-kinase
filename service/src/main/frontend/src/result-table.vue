<template>
    <div>
        <v-data-table
            :headers="headers"
            class="compound-table"
            :items="items"
            :server-items-length="totalItems"
            :options.sync="tableOptions"
            :loading="loading"
            multi-sort
            :footer-props="{ 'items-per-page-options': [ 10, 20, 50, 100 ] }"
        >

            <!-- Custom header rendering is solely to allow HTML in header.text! -->
            <template v-slot:header.kd="{ header }">
                <span v-html="header.text"></span>
            </template>

            <template v-slot:item="{ item }">
                <tr>
                    <td>
                        <a :href="'#/compound/' + item.compoundName">{{item.compoundName}}</a>
                    </td>
                    <td class="text-right">{{item.percentControl}}</td>
                    <td class="text-right">{{item.compoundConcentration}}</td>
                    <td>
                        <a :href="item.kinase.discoverxUrl"
                           target="_blank" rel="noopener noreferrer">
                            {{item.kinase.discoverxGeneSymbol}}</a>
                    </td>
                    <td>{{item.kinase.entrezGeneSymbol}}</td>
                    <td>{{item.kinase.nanosynGeneSymbol}}</td>
                    <td class="text-right">{{item.kd}}</td>
                </tr>
            </template>
        </v-data-table>
    </div>
</template>

<script lang="ts">
import restApi from './rest-api';
import { PagedDataRep, VueDataTableOptions } from '@/rak';

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
            tableOptions: {
                page: 0,
                itemsPerPage: 10,
                sortBy: [ 'percentControl' ],
                sortDesc: [ false ],
                groupBy: [],
                groupDesc: [],
                multiSort: true,
                mustSort: false
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

        tableOptions: {
            handler() {
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

        reloadTable: function() {

            this.loading = true;
            const options: VueDataTableOptions = this.tableOptions;

            let sort: string = '';
            for (let i: number = 0; i < options.sortBy.length; i++) {
                const sortCol: string = options.sortBy[i];
                const sortDir: string = options.sortDesc[i] ? 'desc' : 'asc';
                sort += `${sortCol},${sortDir}`;
                if (i < options.sortBy.length - 1) {
                    sort += '&sort=';
                }
            }

            return restApi.getActivityProfiles(options.page - 1, options.itemsPerPage, this.filters, sort)
                .then((pagedData: PagedDataRep<any>) => {
                    this.items = pagedData.data;
                    this.totalItems = pagedData.total;
                    this.loading = false;
                    return pagedData;
                });
        }
    }
};
</script>
