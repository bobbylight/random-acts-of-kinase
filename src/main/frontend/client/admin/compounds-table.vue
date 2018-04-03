<template>
    <div>
        <v-data-table
            class="compound-table"
            :items="items"
            :search="search"
            :pagination.sync="pagination"
            :total-items="totalItems"
            :loading="loading"
            :headers="createHeaders"
            :rows-per-page-items='[ 20, 50, 100 ]'>
        </v-data-table>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop, Watch } from 'vue-property-decorator';
import axios, { AxiosResponse } from 'axios';
import { PagedDataRep } from '../rak';

export interface ColumnInfo {
    columnName: string;
    columnId: string;
    isCompound?: boolean;
}

/**
 * A table showing information about compounds.  This is really just a wrapper around our table
 * component with niceties built in for tables showing compound-related information.
 */
@Component
export default class CompoundTable extends Vue {

    @Prop({ required: true })
    private url: string;

    @Prop({ required: true })
    private columnInfo: ColumnInfo[];

    private search: string = '';

    private totalItems: number = 0;

    private items: any[] = [];

    private loading: boolean = true;

    private pagination: any = {};

    private compoundRenderer(data: string, type: any, row: number) {
        return `<a href="#/compound/${data}">${data}</a>`;
//                return '<a v-link="{ path: \'/compound/' + data + '\' }">' + data + '</a>';
    }

    private get createHeaders() {

        const headers: any = [];

        this.columnInfo.forEach((colInfo: ColumnInfo) => {
            headers.push({ text: colInfo.columnName, value: colInfo.columnId });
        });

        return headers;
    }

    private get dataTableColumns(): any[] {

        return this.columnInfo.map((colInfo: ColumnInfo) => {
            return {
                data: colInfo.columnId,
                defaultContent: '',
                render: colInfo.isCompound ? this.compoundRenderer : undefined
            };
        });
    }

    // mounted() {
    //
    //     const pageSize: number = 8;
    //
    //     this.table = $(this.$refs.table).DataTable({
    //         serverSide: true,
    //         searching: false,
    //         lengthChange: false,
    //         info: false,
    //         order: [ [ 0, 'asc' ] ],
    //         pageLength: 8,
    //         pagingType: 'first_last_numbers',
    //         ajax: {
    //             url: this.url,
    //             traditional: true,
    //             data: (d) => {
    //
    //                 delete d.columns;
    //                 delete d.search;
    //
    //                 d.page = d.start / pageSize;
    //                 console.log('... requesting page: ' + d.page);
    //                 delete d.start;
    //
    //                 d.size = d.length;
    //                 delete d.length;
    //
    //                 const newOrder: string[] = d.order.map((orderArg) => {
    //                     return this.dataTableColumns[orderArg.column].data + ',' + orderArg.dir;
    //                 });
    //                 d.sort = newOrder;
    //                 delete d.order;
    //
    //                 return d;
    //             },
    //             dataFilter: (data: string) => {
    //
    //                 // Convert the (string) JSON response into that expected by DataTables
    //
    //                 const json: any = JSON.parse(data);
    //                 return JSON.stringify({
    //                     recordsTotal: json.total, // This isn't really true, but I don't think we need to do another query
    //                     recordsFiltered: json.total,
    //                     data: json.data
    //                 });
    //             }
    //         },
    //         columns: this.dataTableColumns,
    //         language: {
    //             paginate: {
    //                 first: '<<',
    //                 previous: '<',
    //                 next: '>',
    //                 last: '>>'
    //             }
    //         }
    //     } );
    // }

    reloadTable() {

        this.loading = true;

        const { sortBy, descending, page, rowsPerPage } = this.pagination;

        const sort: string = sortBy ? `${sortBy},${descending ? 'desc' : 'asc'}` : '';

        return axios.get(this.url)
            .then((response: AxiosResponse<PagedDataRep<any>>) => {
                const pagedData: PagedDataRep<any> = response.data;
                this.items = pagedData.data;
                this.totalItems = pagedData.total;
                this.loading = false;
                return pagedData;
            });
    }

    @Watch('pagination')
    private onPaginationHandlerChanged(newValue: any) {
        console.log('here');
        // Note this triggers an unnecessary second query until
        // https://github.com/vuetifyjs/vuetify/issues/3585 is fixed
        return this.reloadTable();
    }
}
</script>
