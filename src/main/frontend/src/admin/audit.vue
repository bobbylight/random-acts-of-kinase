<template>
    <v-container grid-list-md>
        <v-layout row wrap class="audit-wrapper">

            <section-header>Audit Records</section-header>

            <v-flex xs12>

                <p>
                    See who's done what in the application.
                </p>

                <p>
                    Click on a row to see more detail about the audit record.
                </p>
            </v-flex>

            <v-flex xs12>

                <v-card>

                    <v-card-title class="headline audit-filter-title">Filters</v-card-title>

                    <v-card-text>

                        <v-layout row wrap class="audit-filters-wrapper">

                            <v-flex xs6 class="audit-filter">
                                <v-text-field label="User"
                                              @input="debouncedReloadTable"
                                              prepend-icon="fa-user"
                                              hide-details
                                              clearable
                                              v-model="userFilter"></v-text-field>
                            </v-flex>

                            <v-flex xs6 class="audit-filter">
                                <v-select
                                    label="Action"
                                    required
                                    :items="actions"
                                    v-model="actionFilter"
                                    prepend-icon="fa-exclamation"
                                    hide-details
                                    @input="debouncedReloadTable"
                                ></v-select>
                            </v-flex>

                            <v-flex xs6 class="audit-filter">
                                <v-text-field label="IP Address"
                                              @input="debouncedReloadTable"
                                              prepend-icon="fa-map-marker-alt"
                                              hide-details
                                              clearable
                                              v-model="ipAddressFilter"></v-text-field>
                            </v-flex>

                            <v-flex xs6 class="audit-filter">
                                <v-select
                                    label="Successful"
                                    required
                                    :items="[ { text: 'Any', value: 'any' }, { text: 'Yes', value: 'yes' }, { text: 'No', value: 'no' } ]"
                                    v-model="successFilter"
                                    prepend-icon="done"
                                    hide-details
                                    @input="debouncedReloadTable"
                                ></v-select>
                            </v-flex>

                            <v-flex xs6 class="audit-filter">
                                <v-menu
                                    v-model="showFromDate"
                                    :close-on-content-click="false"
                                    :nudge-right="40"
                                    lazy
                                    transition="scale-transition"
                                    offset-y
                                    full-width
                                    min-width="290px"
                                >
                                    <template v-slot:activator="{ on }">
                                        <v-text-field
                                            v-model="fromDateFilter"
                                            label="From Date (inclusive, UTC)"
                                            prepend-icon="event"
                                            readonly
                                            hide-details
                                            clearable
                                            v-on="on"
                                            @input="debouncedReloadTable"
                                        ></v-text-field>
                                    </template>
                                    <v-date-picker v-model="fromDateFilter"
                                                   min="2018-01-01"
                                                   :max="toDateFilter || maxAllowedDate"
                                                   @input="showFromDate = false; debouncedReloadTable()"></v-date-picker>
                                </v-menu>
                            </v-flex>

                            <v-flex xs6 class="audit-filter">
                                <v-menu
                                    v-model="showToDate"
                                    :close-on-content-click="false"
                                    :nudge-right="40"
                                    lazy
                                    transition="scale-transition"
                                    offset-y
                                    full-width
                                    min-width="290px"
                                >
                                    <template v-slot:activator="{ on }">
                                        <v-text-field
                                            v-model="toDateFilter"
                                            label="To Date (inclusive, UTC)"
                                            prepend-icon="event"
                                            readonly
                                            hide-details
                                            clearable
                                            v-on="on"
                                            @input="debouncedReloadTable"
                                        ></v-text-field>
                                    </template>
                                    <v-date-picker v-model="toDateFilter"
                                                   :min="fromDateFilter"
                                                   :max="maxAllowedDate"
                                                   @input="showToDate = false; debouncedReloadTable()"></v-date-picker>
                                </v-menu>
                            </v-flex>
                        </v-layout>

                        <v-data-table
                            :headers="headers"
                            :items="items"
                            item-key="id"
                            :pagination.sync="pagination"
                            :total-items="totalItems"
                            :loading="loading"
                            :rows-per-page-items='[ 20, 50, 100 ]'
                        >

                            <template slot="items" slot-scope="props">
                                <tr @click="props.expanded = !props.expanded">
                                    <td>{{getDisplayDate(props.item.createDate)}}</td>
                                    <td>{{props.item.userName}}</td>
                                    <td>{{getAuditActionLabel(props.item.action)}}</td>
                                    <td>{{props.item.ipAddress}}</td>
                                    <td>
                                        <v-checkbox
                                            class="audit-enabled-cb"
                                            disabled
                                            v-model="props.item.success"
                                        ></v-checkbox>
                                    </td>
                                </tr>
                            </template>

                            <template slot="expand" slot-scope="props">
                                <v-card flat>
                                    <v-card-text class="audit-details">{{props.item.details || '(no details)'}}</v-card-text>
                                </v-card>
                            </template>
                        </v-data-table>
                    </v-card-text>
                </v-card>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Watch } from 'vue-property-decorator';
import { Audit, PagedDataRep } from '../rak';
import SectionHeader from '../header.vue';
import AuditActions from './audit-actions';
import restApi from '../rest-api';
import rakUtil from '../util';
import debounce from 'debounce';

@Component({ components: { SectionHeader } })
export default class AuditHistory extends Vue {

    private totalItems: number = 0;

    private items: Audit[] = [];

    private loading: boolean = true;

    pagination: any = {
        sortBy: 'createDate',
        descending: true
    };

    private actions: any = [];
    private maxAllowedDate: string = '';

    actionFilter: string = '';
    ipAddressFilter: string = '';
    userFilter: string = '';
    successFilter: string = 'any';
    fromDateFilter: string = '';
    showFromDate: boolean = false;
    toDateFilter: string = '';
    showToDate: boolean = false;

    created() {

        this.actions = AuditActions.getLabelValues();
        this.maxAllowedDate = new Date().toISOString();
        this.maxAllowedDate = this.maxAllowedDate.substring(0, this.maxAllowedDate.indexOf('T'));

        this.debouncedReloadTable = debounce(this.debouncedReloadTable, 750);
    }

    private debouncedReloadTable() {
        return this.reloadTable();
    }

    getAuditActionLabel(action: string): string {
        return AuditActions.getLabel(action);
    }

    getDisplayDate(iso8601Date: string): string {
        return rakUtil.getDisplayDate(iso8601Date, true);
    }

    get headers(): any[] /*VTableHeader[]*/ {

        return [
            { text: 'Date', value: 'createDate' },
            { text: 'User', value: 'userName' },
            { text: 'Action', value: 'action' },
            { text: 'IP Address', value: 'ipAddress' },
            { text: 'Successful?', value: 'success' }
        ];
    }

    reloadTable() {

        this.loading = true;

        const { sortBy, descending, page, rowsPerPage } = this.pagination;

        const sort: string = sortBy ? `${sortBy},${descending ? 'desc' : 'asc'}` : '';

        const filters: any = {
            userName: this.userFilter,
            action: this.actionFilter,
            ipAddress: this.ipAddressFilter,
            success: this.successFilter,
            fromDate: this.fromDateFilter ? this.fromDateFilter : null,
            toDate: this.toDateFilter ? this.toDateFilter : null
        };

        return restApi.getAuditRecords(page - 1, 20, filters, sort)
            .then((pagedData: PagedDataRep<Audit>) => {
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
.audit-wrapper {

    .audit-filter-title {
        padding-bottom: 0;
    }

    .audit-filters-wrapper {
        padding: 0 2rem 2rem 2rem;

        .audit-filter {
            padding: 0 1rem !important;
        }
    }

    .audit-enabled-cb {

        // Center checkboxes vertically and horizontally
        .v-input__slot {

            margin-bottom: 0;

            .v-input--selection-controls__input {
                margin: 0 auto;
            }
        }

        .v-messages { // Empty "messages" section under checkboxes with non-zero height
            display: none;
        }
    }

    .audit-details {
        margin: 0 3rem;
    }
}
</style>
