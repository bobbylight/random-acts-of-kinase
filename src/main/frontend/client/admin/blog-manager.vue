<template>
    <v-container grid-list-md>
        <v-layout row wrap class="blog-manager-wrapper">

            <v-flex xs12>
                <div class="headline pb-2 primary--text">News Posts</div>
            </v-flex>

            <v-flex xs12>

                <v-data-table
                    :headers="headers"
                    class="elevation-1"
                    :items="items"
                    :search="search"
                    :pagination.sync="pagination"
                    :total-items="totalItems"
                    :loading="loading"
                    :rows-per-page-items='[ 20, 50, 100 ]'
                >

                    <template slot="items" slot-scope="props">
                        <blog-manager-post-name-cell :post="props.item"></blog-manager-post-name-cell>
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
import { BlogPost, PagedDataRep } from '../rak';
import BlogManagerPostNameCell from './blog-manager-post-name-cell.vue';
import restApi from '../rest-api';

@Component({ components: { BlogManagerPostNameCell } })
export default class BlogManager extends Vue {

    private search: string = '';

    private totalItems: number = 0;

    private items: any[] = [];

    private loading: boolean = true;

    private pagination: any = {
        sortBy: 'createDate',
        descending: true
    };

    get headers(): any[] /*VTableHeader[]*/ {

        return [
            { text: 'Title', value: 'title' },
            { text: 'Author', value: 'author', sortable: false },
            { text: 'Date', value: 'createDate' }
        ];
    }

    reloadTable() {

        this.loading = true;

        const { sortBy, descending, page, rowsPerPage } = this.pagination;

        const sort: string = sortBy ? `${sortBy},${descending ? 'desc' : 'asc'}` : '';

        return restApi.getBlogPosts(page - 1, 10000, {}, sort)
            .then((pagedData: PagedDataRep<BlogPost[]>) => {
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
