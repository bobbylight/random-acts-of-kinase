<template>
    <v-container grid-list-md>
        <v-layout row wrap class="stats-wrapper">

            <section-header>Feedback</section-header>

            <v-flex xs12>

                <div>
                    Feedback as entered by users.  Hopefully at least some of this can be converted into bug reports
                    or feature requests.
                </div>

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
                    :rows-per-page-items='[ 10, 20, 50 ]'
                >

                    <template slot="items" slot-scope="props">
                        <td>{{props.item.title}}</td>
                        <td>{{props.item.createDate}}</td>
                    </template>
                </v-data-table>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import SectionHeader from '../header.vue';
import { Feedback, PagedDataRep } from '../rak';
import { Watch } from 'vue-property-decorator';
import restApi from '../rest-api';

@Component({ components: { SectionHeader } })
export default class FeedbackManager extends Vue {

    private search: string = '';

    private totalItems: number = 0;

    private items: Feedback[] = [];

    private loading: boolean = true;

    private pagination: any = {
        sortBy: 'createDate',
        descending: true
    };

    get headers(): any[] /*VTableHeader[]*/ {

        return [
            { text: 'Title', value: 'title' },
            { text: 'Date', value: 'createDate' }
        ];
    }

    reloadTable() {

        this.loading = true;

        const { sortBy, descending, page, rowsPerPage } = this.pagination;

        const sort: string = sortBy ? `${sortBy},${descending ? 'desc' : 'asc'}` : '';

        return restApi.getFeedback(page - 1, 10000, {}, sort)
            .then((pagedData: PagedDataRep<Feedback>) => {
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
