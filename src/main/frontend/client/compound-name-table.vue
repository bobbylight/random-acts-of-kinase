<template>
    <div>
        <v-data-table
            hide-headers
            class="compound-name-table"
            :items="items"
            :search="search"
            :pagination.sync="pagination"
            :total-items="totalItems"
            :loading="loading"
            :rows-per-page-items='[ 20, 50, 100 ]'
            >

            <template slot="items" slot-scope="props">
                <td>
                    <div class="compound-name-cell">
                        <img class="b-lazy"
                             src="data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw=="
                             :data-src="getCompoundImage(props.item.compoundName)"
                             width=40 height=40>
                        &nbsp;&nbsp;&nbsp;
                        <div class="compoundDesc">
                            <a class="compoundName" :href="getCompoundUrl(props.item.compoundName)">{{props.item.compoundName}}</a><br>
                            <span class="chemotype">{{props.item.chemotype || '...'}}, s(10): {{props.item.s10 || '?'}}</span>
                        </div>
                    </div>
                </td>
            </template>
        </v-data-table>
    </div>
</template>

<script>
import restApi from 'rest-api';
import Blazy from 'blazy';

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
            search: '',
            totalItems: 0,
            items: [],
            loading: true,
            pagination: {}
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
        },

        pagination: {
            handler () {
                // Note this triggers an unnecessary second query until
                // https://github.com/vuetifyjs/vuetify/issues/3585 is fixed
                this.reloadTable();
            },
            deep: true
        }
    },
    methods: {

        getCompoundImage: function(compoundName) {
            return `api/compounds/images/${compoundName}`;
        },

        getCompoundUrl: function(compoundName) {
            return `#/compound/${compoundName}`;
        },

        reloadTable: function() {

            this.loading = true;

            const { sortBy, descending, page, rowsPerPage } = this.pagination;

            return restApi.getCompounds(page - 1, rowsPerPage, this.filters)
                .then(pagedData => {
                    this.items = pagedData.data;
                    this.totalItems = pagedData.total;
                    this.loading = false;
                    return pagedData;
                });
        }
    },
    updated: function() {
        new Blazy({
            container: 'compound-name-table'
        });
        // this.$el.querySelectorAll('img.lazy').forEach(lazyImage => lazyImage.Lazy({ chainable: false }).update();
    }
};
</script>

<style lang="less">

    @transition-time: .5s;

    .compound-name-table {
        thead {
            display: none;
        }

        tr {

            transition: background-color @transition-time;

            .compoundDesc {
                vertical-align: middle;
                display: inline-block;

                .compoundName {
                    text-decoration: none;
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

            div.compound-name-cell {
                font-size: 1.5rem;
                padding: 1rem 0;
            }
        }

        border: none;

        img {
            vertical-align: middle;
        }
    }
</style>
