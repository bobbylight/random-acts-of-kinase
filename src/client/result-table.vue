<template>
    <div>
       <table id="compound-table" class="ui celled table" width="100%">
            <thead>
                <tr>
                    <th>Compound</th>
                    <th>% Control</th>
                    <th>Concentration (nM)</th>
                    <th>DiscX Gene Symbol</th>
                    <th>Entrez Gene Symbol</th>
                    <th>K<sub>d</sub></th>
                </tr>
            </thead>
        </table>
    </div>
</template>

<script>

    import $ from 'jquery';

    export default {
        name: 'result-table',
        props: {
            //inhibitor: String,
            filters: {
                type: Object,
                required: true
            }
        },
        data: function() {
            return {
                table: null
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
            }
        },
        methods: {

            addSuppliedFilters: function(data) {
                if (this.filters.inhibitor) {
                    data.inhibitor = this.filters.inhibitor;
                }
                if (this.filters.activity) {
                    data.activity = this.filters.activity;
                }
                if (this.filters.kinase) {
                    data.kinase = this.filters.kinase;
                }
            },

            reloadTable: function() {
                this.table.ajax.reload();
            },

            compoundRenderer: function(data, type, row) {
                return '<a href="#/compound/' + data + '">' + data + '</a>';
//                return '<a v-link="{ path: \'/compound/' + data + '\' }">' + data + '</a>';
            }
        },
        mounted: function() { // tslint:disable-line

            // Limit number of page buttons in pagination widget
            $.fn.dataTable.ext.pager.numbers_length = 4;

            const columns = [
                { 'data': 'compound_nm', render: this.compoundRenderer },
                { 'data': 'percent_control' },
                { 'data': 'compound_concentration_nm' },
                { 'data': 'discoverx_gene_symbol' },
                { 'data': 'entrez_gene_symbol' },
                { 'data': 'kd_nm' }
            ];

            const that = this;
            this.table = $('#compound-table').DataTable({
                serverSide: true,
                searching: false,
                lengthChange: false,
                info: false,
                order: [ [ 5, 'asc' ], [ 1, 'asc' ] ],
                pageLength: 20,
                pagingType: 'first_last_numbers',
                ajax: {
                    url: '/api/compounds',
                    data: (d) => {

                        this.addSuppliedFilters(d);
                        delete d.columns;
                        delete d.search;

                        d.offset = d.start;
                        delete d.start;

                        d.limit = d.length;
                        delete d.length;

                        const newOrder = d.order.map((orderArg) => {
                            return columns[orderArg.column].data + ':' + orderArg.dir;
                        }).join(',');
                        d.order = newOrder;

                        return d;
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
    };
</script>
