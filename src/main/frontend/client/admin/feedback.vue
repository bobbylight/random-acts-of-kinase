<template>
    <v-container grid-list-md>
        <v-layout row wrap class="stats-wrapper">

            <section-header>Feedback</section-header>

            <v-flex xs12>

                <div>
                    <p>
                    Feedback as entered by users.  Hopefully at least some of this can be converted into bug reports
                    or feature requests.
                    </p>

                    <p>
                    Click on a row to see more detail about the feedback.
                    </p>
                </div>

            </v-flex>

            <v-flex xs12>

                <v-btn @click="onReply" :disabled="isReplyDisabled()">
                    Reply
                </v-btn>

                <v-btn @click="onCreateTicket" :disabled="selectedFeedback.length !== 1">
                    Create GitHub ticket
                </v-btn>

                <v-btn @click="onDelete" :disabled="selectedFeedback.length === 0">
                    Delete
                </v-btn>
            </v-flex>

            <v-flex xs12>

                <v-data-table
                    :headers="headers"
                    class="elevation-1"
                    v-model="selectedFeedback"
                    :items="items"
                    item-key="id"
                    select-all
                    :search="search"
                    :pagination.sync="pagination"
                    :total-items="totalItems"
                    :loading="loading"
                    :rows-per-page-items='[ 10, 20, 50 ]'
                >

                    <template slot="items" slot-scope="props">
                        <tr @click="props.expanded = !props.expanded">
                            <td>
                                <v-checkbox
                                    primary
                                    hide-details
                                    v-model="props.selected"
                                ></v-checkbox>
                            </td>
                            <td v-html="linkifyEmail(props.item)"></td>
                            <td>{{props.item.title}}</td>
                            <td>{{new Date(props.item.createDate).toLocaleString()}}</td>
                        </tr>
                    </template>

                    <template slot="expand" slot-scope="props">
                        <v-card flat>
                            <v-card-text>{{props.item.body}}</v-card-text>
                        </v-card>
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
import Toaster from '../toaster';
import rakUtil from '../util';

@Component({ components: { SectionHeader } })
export default class FeedbackManager extends Vue {

    private selectedFeedback: any[] = [];

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
            { text: 'E-mail', value: 'email' },
            { text: 'Title', value: 'title' },
            { text: 'Date', value: 'createDate' }
        ];
    }

    isReplyDisabled(): boolean {
        return this.selectedFeedback.length !== 1 || !this.selectedFeedback[0].email;
    }

    /**
     * Creates a <code>mailto:</code> URL to response to a feedback item.  It is assumed
     * that the feedback's email has previously been verified as  non-<code>null</code>.
     *
     * @param {Feedback} feedback The feedback.
     * @returns {string} The <code>mailto:</code> URL.
     */
    private createMailtoUrl(feedback: Feedback): string {
        const email: string = feedback.email!;
        const subject: string = `Random Acts of Kinase - Feedback &quot;${encodeURIComponent(feedback.title)}&quot;`;
        return `mailto:${email}?subject=${subject}`;
    }

    linkifyEmail(feedback: Feedback): string {

        if (!feedback.email) {
            return '';
        }
        return `<a href="${this.createMailtoUrl(feedback)}">${feedback.email}</a>`;
    }

    onCreateTicket() {
        Toaster.error('Not yet implemented');
    }

    onDelete() {
        Toaster.error('Not yet implemented');
    }

    onReply() {
        const feedback: Feedback = this.selectedFeedback[0];
        rakUtil.programmaticallyClickLink(this.createMailtoUrl(feedback));
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
