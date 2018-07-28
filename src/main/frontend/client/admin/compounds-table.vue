<template>
    <div>
        <v-data-table
            class="compound-table"
            :items="items"
            :pagination.sync="pagination"
            :total-items="totalItems"
            :loading="loading"
            :headers="createHeaders"
            :rows-per-page-items='[ 10, 20, 50 ]'>

            <template slot="items" slot-scope="props">
                <td v-for="header in createHeaders" :class="getClassesForRow(header.value)">
                    <a v-if="header.value === 'compoundName'" :href="getCompoundLink(props.item[header.value])">
                        {{props.item[header.value]}}
                    </a>
                    <span v-if="header.value !== 'compoundName'">
                        {{props.item[header.value]}}
                    </span>
                </td>
            </template>

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

    private totalItems: number = 0;

    private items: any[] = [];

    private loading: boolean = true;

    private pagination: any = {};

    private getCompoundLink(compoundName: string) {
        return `#/compound/${compoundName}`;
    }

    private get createHeaders() {

        const headers: any = [];

        this.columnInfo.forEach((colInfo: ColumnInfo) => {

            const header: any = { text: colInfo.columnName, value: colInfo.columnId };
            if (colInfo.columnId === 's10') {
                header.align = 'right';
            }

            headers.push(header);
        });

        return headers;
    }

    /**
     * Returns the CSS classes to apply to a cell for the given column.
     *
     * @param {string} columnId
     * @returns {string}
     */
    private getClassesForRow(columnId: string): string | undefined {
        // TODO: Extract this hard-coded knowledge of numeric columns somewhere else
        if ('s10' === columnId) {
            return 'text-xs-right';
        }
        return undefined;
    }

    reloadTable() {

        this.loading = true;

        const { sortBy, descending, page, rowsPerPage } = this.pagination;

        const sort: string = sortBy ? `${sortBy},${descending ? 'desc' : 'asc'}` : '';

        let url: string = `${this.url}?page=${page - 1}&size=${rowsPerPage}`;
        if (sort) {
            url += `&sort=${sort}`;
        }

        return axios.get(url)
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
        // Note this triggers an unnecessary second query until
        // https://github.com/vuetifyjs/vuetify/issues/3585 is fixed
        return this.reloadTable();
    }
}
</script>
