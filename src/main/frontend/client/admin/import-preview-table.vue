<template>
    <div>
        <v-data-table
            class="import-preview-table"
            :items="items"
            :headers="createHeaders()"
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
import { Prop } from 'vue-property-decorator';
import { FieldStatus } from '../rak';

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
    private items: any[];

    @Prop({ required: true })
    private columnInfo: ColumnInfo[];

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

        //// TODO: Extract this hard-coded knowledge of numeric columns somewhere else
        //if ('s10' === columnId) {
        //    return 'text-xs-right';
        //}

        if (value.oldValue) {
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
}
</script>

<style lang="less">
</style>
