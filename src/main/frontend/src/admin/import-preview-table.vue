<template>
    <div>
        <v-data-table
            class="import-preview-table"
            :loading="loading"
            :no-data-text="noDataText"
            :items="items"
            :headers="createHeaders()"
            :custom-sort="sortGrid"
            :options.sync="tableOptions"
            multi-sort
            :footer-props="{ 'items-per-page-options': [ 10, 20, 50 ] }"
        >

            <!-- Custom header rendering is solely to allow HTML in header.text! -->
            <template v-slot:header.kd="{ header }">
                <span v-html="header.text"></span>
            </template>

            <template v-slot:item="{ item }">
                <tr>
                    <td v-for="header in createHeaders()"
                        :class="getClassesForRow(header.value, item[header.value])">
                        <v-tooltip bottom>
                            <template v-slot:activator="{ on }">
                                <span v-on="on">{{item[header.value].newValue}}</span>
                            </template>
                            <span>{{getToolTip(item[header.value])}}</span>
                        </v-tooltip>
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
import { FieldStatus, VueDataTableOptions } from '@/rak';

export interface ColumnInfo {
    name: string;
    value: string;
    isNumber?: boolean;
}

/**
 * A table showing information about an import.
 */
@Component
export default class ImportPreviewTable extends Vue {

    @Prop({ required: true })
    private readonly items: any[];

    @Prop({ required: true })
    private readonly columnInfo: ColumnInfo[];

    @Prop({ required: false, default: false })
    private readonly loading: boolean;

    tableOptions: VueDataTableOptions = {
        page: 0,
        itemsPerPage: 10,
        sortBy: [],
        sortDesc: [],
        groupBy: [],
        groupDesc: [],
        multiSort: true,
        mustSort: false
    };

    private createHeaders(): any[] {

        const headers: any = [];

        this.columnInfo.forEach((colInfo: ColumnInfo) => {

            const header: any = {
                text: colInfo.name,
                value: colInfo.value
            };
            if (colInfo.isNumber) {
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
    private getClassesForRow(columnId: string, value: FieldStatus): string | undefined {

        let classes: string = '';

        if (value.oldValue != null) {
            if (value.oldValue !== value.newValue) {
                classes += 'orange--text ';
            }
            else {
                classes += 'grey--text ';
            }
        }
        else {
            classes += 'success--text ';
        }

        return classes.length ? classes : undefined;
    }

    private getToolTip(value: FieldStatus): string {

        if (value.oldValue) {
            if (value.oldValue !== value.newValue) {
                return `Prior value: ${value.oldValue}`;
            }
            return 'This value is unchanged';
        }

        return 'This is a new value';
    }

    private get noDataText(): string {
        return this.loading ? 'Loading...' : 'No data available';
    }

    private sortGrid(items: object[], sortBy: string[], sortDesc: boolean[]): object[] {

        if (!sortBy || !sortBy.length) {
            return items;
        }

        return items.sort((a: any, b: any) => {

            for (let i: number = 0; i < sortBy.length; i++) {

                const sortCol: string = sortBy[i];
                const descending: boolean = sortDesc[i];

                if (descending) {
                    const temp: any = b;
                    b = a;
                    a = temp;
                }

                let aValue: string | number | null | undefined = a[sortCol].newValue;
                let bValue: string | number | null | undefined = b[sortCol].newValue;

                if (typeof aValue === 'string') {
                    aValue = aValue.toLowerCase();
                }
                if (typeof bValue === 'string') {
                    bValue = bValue.toLowerCase();
                }

                if (aValue == null) {
                    aValue = '';
                }
                if (bValue == null) {
                    bValue = '';
                }

                const sortResult: number =  aValue < bValue ? -1 : (aValue > bValue ? 1 : 0);
                if (sortResult !== 0) {
                    return sortResult;
                }
            }

            // All sorted-on columns compared equally
            return 0;
        });
    }
}
</script>

<style lang="less">
</style>
