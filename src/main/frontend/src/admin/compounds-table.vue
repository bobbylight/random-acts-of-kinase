<template>
    <div>
        <v-data-table
            class="compound-table"
            :items="items"
            :items-per-page="tableOptions.itemsPerPage"
            :server-items-length="totalItems"
            :options.sync="tableOptions"
            :loading="loading"
            multi-sort
            :headers="createHeaders"
            :footer-props="{ 'items-per-page-options': [ 10, 20, 50 ] }"
        >

            <template v-slot:item="{ item }">
                <tr>
                    <td v-for="header in createHeaders" :class="getClassesForRow(header.value)">
                        <a v-if="header.value === 'compoundName'" :href="getCompoundLink(item[header.value])">
                            {{item[header.value]}}
                        </a>
                        <a v-else-if="header.value.indexOf('Url') > -1" :href="item[header.value]" target="_blank"
                                rel="noopener noreferrer">
                            {{item[header.value]}}
                        </a>
                        <span v-else-if="header.value === 'hidden'">
                            <v-checkbox
                                class="audit-enabled-cb"
                                disabled
                                v-model="item[header.value]"
                            ></v-checkbox>
                       </span>
                        <span v-else>
                            {{item[header.value]}}
                        </span>
                    </td>
                </tr>
            </template>

        </v-data-table>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop, Watch } from 'vue-property-decorator';
import axios, { AxiosResponse } from 'axios';
import debounce from 'debounce';
import { PagedDataRep, VueDataTableOptions } from '@/rak';

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
    private readonly url: string;

    @Prop({ required: true })
    private readonly columnInfo: ColumnInfo[];

    private totalItems: number = 0;

    private items: any[] = [];

    private loading: boolean = true;

    tableOptions: VueDataTableOptions = {
        page: 1,
        itemsPerPage: 10,
        sortBy: [ 'compoundName' ],
        sortDesc: [ false ],
        groupBy: [],
        groupDesc: [],
        multiSort: true,
        mustSort: false
    };

    private getCompoundLink(compoundName: string) {
        return `#/compound/${compoundName}`;
    }

    created() {
        this.debouncedReloadTable = debounce(this.debouncedReloadTable, 750);
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
     * A debounced call to <code>reloadTable()</code>.  We must do this because you
     * cannot debounce a watch function (somehow it's "too late") so we have to be a
     * little indirect to ensure debouncing.
     */
    private debouncedReloadTable(newUrl: string) {
        console.log(`Reloading table due to url change to: ${newUrl}`);
        return this.reloadTable();
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
            return 'text-right';
        }
        return undefined;
    }

    reloadTable() {

        this.loading = true;
        const options: VueDataTableOptions = this.tableOptions;

        let sort: string = '';
        for (let i: number = 0; i < options.sortBy.length; i++) {
            const sortCol: string = options.sortBy[i];
            const sortDir: string = options.sortDesc[i] ? 'desc' : 'asc';
            sort += `${sortCol},${sortDir}`;
            if (i < options.sortBy.length - 1) {
                sort += '&sort=';
            }
        }

        const firstParamChar: string = this.url.indexOf('?') > -1 ? '&' : '?';

        let url: string = `${this.url}${firstParamChar}page=${options.page - 1}&size=${options.itemsPerPage}`;
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

    @Watch('tableOptions')
    private onTablePagingOrSortingChanged(newOptions: VueDataTableOptions) {
        return this.reloadTable();
    }

    @Watch('url')
    private onUrlChanged(newValue: string) {
        return this.debouncedReloadTable(newValue);
    }
}
</script>
