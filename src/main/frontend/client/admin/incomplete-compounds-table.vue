<template>
    <div>
        <table ref="table" class="ui celled table" width="100%">
            <thead>
            <tr>
                <th>Compound</th>
                <th>Chemotype</th>
                <th>s(10)</th>
                <th>SMILES</th>
                <th>Source</th>
            </tr>
            </thead>
        </table>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import $ from 'jquery';

@Component
export default class IncompleteCompoundsTable extends Vue {

    private table: any;

    private compoundRenderer(data, type, row) {
        return '<a href="#/compound/' + data + '">' + data + '</a>';
//                return '<a v-link="{ path: \'/compound/' + data + '\' }">' + data + '</a>';
    }

    mounted() {

        $.fn.dataTable.ext.pager.numbers_length = 4;

        const columns: any = [
            { data: 'compoundName', render: this.compoundRenderer },
            { data: 'chemotype', defaultContent: '' },
            { data: 's10', defaultContent: '' },
            { data: 'smiles', defaultContent: '' },
            { data: 'source', defaultContent: '' }
        ];

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
                url: '/admin/api/incompleteCompounds',
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
                        return columns[orderArg.column].data + ',' + orderArg.dir;
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
            columns: columns,
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
