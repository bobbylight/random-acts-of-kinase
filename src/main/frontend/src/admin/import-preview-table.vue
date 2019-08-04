<template>
    <div>
        <v-data-table
            class="import-preview-table"
            :loading="loading"
            :no-data-text="noDataText"
            :items="items"
            :headers="createHeaders()"
            :custom-sort="sortGrid"
            :rows-per-page-items='[ 10, 20, 50 ]'>

            <template slot="items" slot-scope="props">
                <td v-for="header in createHeaders()"
                    :class="getClassesForRow(header.value, props.item[header.value])">
                    <v-tooltip bottom>
                        <span slot="activator">
                            {{props.item[header.value].newValue}}
                        </span>
                        <span>{{getToolTip(props.item[header.value])}}</span>
                    </v-tooltip>
                </td>
            </template>
        </v-data-table>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop, Watch } from 'vue-property-decorator';
import { FieldStatus } from '@/rak';

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

    private sortGrid(items: object[], sortCol: string, descending: boolean): object[] {

        if (!sortCol) {
            return items;
        }

        return items.sort((a: any, b: any) => {

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

            return aValue < bValue ? -1 : (aValue > bValue ? 1 : 0);
        });
    }
}
</script>

<style lang="less">
</style>
