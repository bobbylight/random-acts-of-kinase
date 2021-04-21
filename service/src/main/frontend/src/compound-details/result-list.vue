<template>
    <div>

        <v-row>
            <v-col cols="12">

                <v-select v-model="tableOptions.sortBy" :items="headers"
                          style="max-width: 16rem; margin-right: 2rem; display: inline-block;"
                          label="Sort by...">
                      <template v-slot:item="{ item }">
                          <!-- Hack to get K<sub>d</sub> to render as HTML -->
                          <span v-html="item.text"/>
                      </template>
                </v-select>

                <v-btn fab small color="primary" dark @click="ascending = !ascending">
                    <v-icon dark>{{ ascending? 'mdi-arrow-up' : 'mdi-arrow-down' }}</v-icon>
                </v-btn>
            </v-col>
        </v-row>

        <v-data-iterator
            :items="items"
            :server-items-length="totalItems"
            :options.sync="tableOptions"
            :loading="loading"
            :footer-props="{ 'items-per-page-options': [ 5, 10, 20, 50, 100 ] }"
        >

            <template v-slot:default="{ items }">

                <v-card flat>
                    <v-card-text>
                        <v-row>
                            <v-col
                                v-for="item in items"
                                :key="item.kinase.discoverxGeneSymbol"
                                cols="12">

                                <div class="result-list-table">

                                    <div class="result-list-table-row">
                                        <div class="result-list-table-cell result-list-table-cell-header">Compound:</div>
                                        <div class="result-list-table-cell">{{ item.compoundName }}</div>
                                    </div>

                                    <div class="result-list-table-row">
                                        <div class="result-list-table-cell result-list-table-cell-header">% Control:</div>
                                        <div class="result-list-table-cell">{{ item.percentControl }}</div>
                                    </div>

                                    <div class="result-list-table-row">
                                        <div class="result-list-table-cell result-list-table-cell-header">Concentration (nM):</div>
                                        <div class="result-list-table-cell">{{ item.compoundConcentration }}</div>
                                    </div>

                                    <div class="result-list-table-row">
                                        <div class="result-list-table-cell result-list-table-cell-header">Kinase DiscoverX:</div>
                                        <div class="result-list-table-cell">
                                            <new-tab-link :href="item.kinase.discoverxUrl" :label="item.kinase.discoverxGeneSymbol"/>
                                        </div>
                                    </div>

                                    <div class="result-list-table-row">
                                        <div class="result-list-table-cell result-list-table-cell-header">Kinase Entrez:</div>
                                        <div class="result-list-table-cell">{{ item.kinase.entrezGeneSymbol }}</div>
                                    </div>

                                    <div class="result-list-table-row">
                                        <div class="result-list-table-cell result-list-table-cell-header">Kinase Nanosyn:</div>
                                        <div class="result-list-table-cell">{{ item.kinase.nanosynGeneSymbol }}</div>
                                    </div>

                                    <div class="result-list-table-row">
                                        <div class="result-list-table-cell result-list-table-cell-header">K<sub>d</sub>:</div>
                                        <div class="result-list-table-cell">{{ item.kd }}</div>
                                    </div>
                                </div>
                            </v-col>
                        </v-row>
                    </v-card-text>
                </v-card>
            </template>
        </v-data-iterator>
    </div>
</template>

<script lang="ts">
import restApi from '../rest-api';
import { PagedDataRep, VueDataTableOptions } from '@/rak';
import NewTabLink from '@/new-tab-link.vue';

export default {
    name: 'result-list',
    components: {NewTabLink},
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
        },
        sortOptions() {
            return this.headers.map((header: any) => header.value);
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
            ascending: true,
            totalItems: 0,
            items: [],
            loading: true,
            tableOptions: {
                page: 1,
                itemsPerPage: 5,
                sortBy: 'percentControl',
                groupBy: [],
                groupDesc: [],
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
        },

        ascending() {
            this.reloadTable();
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
            const dir: string = this.ascending ? 'asc' : 'desc';
            const sort: string = `&sort=${options.sortBy},${dir}`;

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


<style lang="less">

.result-list-table {

    display: table;
    table-layout: fixed;
    width: 100%;
    padding: 0 1rem 1rem 1rem;
    border-bottom: 1px solid lightgray;

    .result-list-table-row {

        display: table-row;

        .result-list-table-cell {

            display: table-cell;
            padding: 0;
            text-align: end;

            &.result-list-table-cell-header {
                font-weight: bold;
                white-space: nowrap;
                text-align: start;
            }
        }
    }
}
</style>
