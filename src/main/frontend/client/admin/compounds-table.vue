<template>
    <div>
        <table ref="table" class="ui celled table" width="100%">
            <thead>
            <tr>
                <th v-for="colInfo in columnInfo">{{colInfo.columnName}}</th>
            </tr>
            </thead>
        </table>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';
import $ from 'jquery';

export interface ColumnInfo {
    columnName: string;
    columnId: string;
    isCompound?: boolean;
}

/**
 * A table showing information about compounds.  This is really just a wrapper around our table
 * component with niceities built in for tables showing compound-related information.
 */
@Component
export default class CompoundTable extends Vue {

    @Prop({ required: true })
    private url: string;

    @Prop({ required: true })
    private columnInfo: ColumnInfo[];

    private table: any;

    private compoundRenderer(data: string, type: any, row: number) {
        return `<a href="#/compound/${data}">${data}</a>`;
//                return '<a v-link="{ path: \'/compound/' + data + '\' }">' + data + '</a>';
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

    mounted() {

        $.fn.dataTable.ext.pager.numbers_length = 4;

        const pageSize: number = 8;

        this.table = $(this.$refs.table).DataTable({
            serverSide: true,
            searching: false,
            lengthChange: false,
            info: false,
            order: [ [ 0, 'asc' ] ],
            pageLength: 8,
            pagingType: 'first_last_numbers',
            ajax: {
                url: this.url,
                traditional: true,
                data: (d) => {

                    delete d.columns;
                    delete d.search;

                    d.page = d.start / pageSize;
                    console.log('... requesting page: ' + d.page);
                    delete d.start;

                    d.size = d.length;
                    delete d.length;

                    const newOrder: string[] = d.order.map((orderArg) => {
                        return this.dataTableColumns[orderArg.column].data + ',' + orderArg.dir;
                    });
                    d.sort = newOrder;
                    delete d.order;

                    return d;
                },
                dataFilter: (data) => {

                    // Convert the (string) JSON response into that expected by DataTables

                    const json = JSON.parse(data);
                    return JSON.stringify({
                        recordsTotal: json.total, // This isn't really true, but I don't think we need to do another query
                        recordsFiltered: json.total,
                        data: json.data
                    });
                }
            },
            columns: this.dataTableColumns,
            language: {
                paginate: {
                    first: '<<',
                    previous: '<',
                    next: '>',
                    last: '>>'
                }
            }
        } );
    }
}
</script>
