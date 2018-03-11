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
                { data: 'compoundName', render: this.compoundRenderer },
                { data: 'percentControl' },
                { data: 'compoundConcentration' },
                { data: 'kinase.discoverxGeneSymbol' },
                { data: 'kinase.entrezGeneSymbol' },
                { data: 'kd', defaultContent: '' }
            ];

            const pageSize = 20;

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
                    url: '/api/activityProfiles',
                    traditional: true,
                    data: (d) => {

                        this.addSuppliedFilters(d);
                        delete d.columns;
                        delete d.search;

                        d.page = d.start / pageSize;
                        console.log('... requesting page: ' + d.page);
                        delete d.start;

                        d.size = d.length;
                        delete d.length;

                        const newOrder = d.order.map((orderArg) => {
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
    };
</script>
