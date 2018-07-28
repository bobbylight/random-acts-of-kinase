<template>
    <v-container grid-list-md>
        <v-layout row wrap class="blog-manager-wrapper">

            <section-header>Audit Records</section-header>

            <v-flex xs12>

                <p>
                    See who's done what in the application.
                </p>

            </v-flex>

            <v-flex xs12>

                <v-data-table
                    :headers="headers"
                    class="elevation-1"
                    :items="items"
                    :pagination.sync="pagination"
                    :total-items="totalItems"
                    :loading="loading"
                    :rows-per-page-items='[ 20, 50, 100 ]'
                >

                    <template slot="items" slot-scope="props">
                        <td>{{getDisplayDate(props.item.createDate)}}</td>
                        <td>{{props.item.userName}}</td>
                        <td>{{props.item.action}}</td>
                        <td>{{props.item.ipAddress}}</td>
                        <td>
                            <v-checkbox
                                disabled
                                v-model="props.item.success"
                            ></v-checkbox>
                        </td>
                        <td>{{props.item.details}}</td>
                    </template>
                </v-data-table>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Watch } from 'vue-property-decorator';
import { Audit, PagedDataRep } from '../rak';
import SectionHeader from '../header.vue';
import restApi from '../rest-api';
import rakUtil from '../util';

@Component({ components: { SectionHeader } })
export default class AuditHistory extends Vue {

    private totalItems: number = 0;

    private items: Audit[] = [];

    private loading: boolean = true;

    private pagination: any = {
        sortBy: 'createDate',
        descending: true
    };

    getDisplayDate(iso8601Date: string): string {
        return rakUtil.getDisplayDate(iso8601Date, true);
    }

    get headers(): any[] /*VTableHeader[]*/ {

        return [
            { text: 'Date', value: 'createDate' },
            { text: 'User', value: 'userName' },
            { text: 'Action', value: 'action' },
            { text: 'IP Address', value: 'ipAddress' },
            { text: 'Successful?', value: 'success' },
            { text: 'Details', value: 'details' },
        ];
    }

    reloadTable() {

        this.loading = true;

        const { sortBy, descending, page, rowsPerPage } = this.pagination;

        const sort: string = sortBy ? `${sortBy},${descending ? 'desc' : 'asc'}` : '';

        return restApi.getAuditRecords(page - 1, 20, {}, sort)
            .then((pagedData: PagedDataRep<Audit>) => {
                this.items = pagedData.data;
                this.totalItems = pagedData.total;
                this.loading = false;
                return pagedData;
            });
    }

    @Watch('pagination')
    private onPaginationHandlerChanged(newValue: any) {
        // Note this triggers an unnecessary second query until
        // https://github.com/vuetifyjs/vuetify/issues/3585 is fixed
        return this.reloadTable();
    }
}
</script>

<style lang="less">
</style>
