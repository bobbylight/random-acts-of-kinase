<template>
    <div>
        <table id="compound-name-table" class="ui celled table" width="100%">
        </table>
    </div>
</template>

<script>

    import $ from 'jquery';

    export default {
        name: 'compound-name-table',
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

                const imgSize = 40;
                const url = `#/compound/${data}`;
                const metadata = `${row.chemotype || '...'}, s(10): ${row.s10 || '?'}`;

                return `<div><img src="img/molecule.svg" width="${imgSize}" height="${imgSize}">` +
                    `&nbsp;&nbsp;&nbsp;` +
                    `<div class="compoundDesc">` +
                    `   <a class="compoundName" href="${url}">${data}</a><br>` +
                    `   <span class="chemotype">${metadata}</span>` +
                    '</div>';
            }
        },
        mounted: function() { // tslint:disable-line

            // Limit number of page buttons in pagination widget
            $.fn.dataTable.ext.pager.numbers_length = 4;

            const pageSize = 20;

            const that = this;
            this.table = $('#compound-name-table').DataTable({
                serverSide: true,
                searching: false,
                lengthChange: false,
                info: false,
                pageLength: pageSize,
                pagingType: 'first_last_numbers',
                ajax: {
                    url: '/api/compounds',
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

                        if (that.table) {
                            d.sort = d.order.map((orderArg) => {
                                return that.table.column(orderArg.column).dataSrc() + ',' + orderArg.dir;
                            });
                        }
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
                columns: [
                    { 'data': 'compoundName', render: this.compoundRenderer }
                ],
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

<style lang="less">

    @transition-time: .5s;

    #compound-name-table {
        thead {
            display: none;
        }

        tr {

            transition: background-color @transition-time;

            .compoundDesc {
                vertical-align: middle;
                display: inline-block;

                .compoundName {
                    color: gray;
                    transition: color @transition-time;
                }
                .chemotype {
                    font-size: medium;
                    color: lightgray;
                    transition: color @transition-time;
                }
            }

            &:hover {
                background-color: #f8f8f8;

                .compoundName {
                    color: #8080a0;
                }
                .chemotype {
                    color: #8080a0;
                }
            }
        }

        border: none;
        font-size: 1.5rem;

        img {
            vertical-align: middle;
        }
    }
</style>
