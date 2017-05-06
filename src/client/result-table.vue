<template>
    <div>
       <table id="compound-table" class="ui celled table">
            <thead>
                <tr>
                    <th>Compound</th>
                    <th>% Control</th>
                    <th>Concentration (nM)</th>
                </tr>
            </thead>
        </table>
    </div>
</template>

<script>

    import $ from 'jquery';

    export default {
        template: '#appTemplate',
        name: 'result-table',
        props: {
            filter: {
                type: String,
                required: true
            }
        },
        data: function() {
            return {
                table: null
            };
        },
        watch: {
            filter: function(newFilter) {
                this.table.ajax.reload();
            }
        },
        mounted: function() { // tslint:disable-line
            this.table = $('#compound-table').DataTable({
                serverSide: true,
                searching: false,
                lengthChange: false,
                info: false,
                pageLength: 20,
                ajax: {
                    url: 'api/compounds',
                    data: (d) => {
                        d.filter = this.filter;
                        delete d.columns;
                        delete d.search;
                        d.offset = d.start;
                        delete d.start;
                        d.limit = d.length;
                        delete d.length;
                        return d;
                    }
                },
                columns: [
                    { 'data': 'compound_nm' },
                    { 'data': 'percent_control' },
                    { 'data': 'compound_concentration_nm' }
                ]
            } );
        }
    };
</script>

