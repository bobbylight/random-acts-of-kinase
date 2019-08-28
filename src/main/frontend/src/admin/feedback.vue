<template>
    <v-container grid-list-md>
        <v-layout row wrap class="feedback-wrapper">

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

            <v-flex xs12 class="feedback-button-section">

                <v-btn class="feedback-button" @click="onReply" :disabled="isReplyDisabled()">
                    Reply
                </v-btn>

                <v-btn class="feedback-button" @click="onCreateTicket" :disabled="loading || selectedFeedback.length !== 1">
                    Create GitHub ticket
                </v-btn>

                <v-btn class="feedback-button" @click="onDelete" :disabled="loading || selectedFeedback.length === 0">
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
                    show-select
                    :server-items-length="totalItems"
                    :options.sync="tableOptions"
                    :loading="loading"
                    multi-sort
                    show-expand
                    :single-expand="true"
                    :footer-props="{ 'items-per-page-options': [ 10, 20, 50 ] }"
                >

                    <template v-slot:item.selected="{ item }">
                        <v-checkbox
                            primary
                            hide-details
                            v-model="item.selected"
                        ></v-checkbox>
                    </template>

                    <template v-slot:item.email="{ item }">
                        <span v-html="linkifyEmail(item)"></span>
                    </template>

                    <template v-slot:item.createDate="{ item }">
                        {{new Date(item.createDate).toLocaleString()}}
                    </template>

                    <template v-slot:expanded-item="{ item, headers }">
                        <td :colspan="headers.length">
                            <span class="feedback-body">{{item.body || '(no feedback data)'}}</span>
                        </td>
                    </template>
                </v-data-table>
            </v-flex>
        </v-layout>

        <v-dialog v-model="showConfirmDeleteModal" max-width="500px">
            <v-card>
                <v-card-title class="headline">
                    Confirm Delete Feedback
                </v-card-title>

                <v-card-text>
                    Are you sure you want to delete the selected feedback items?
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="primary" @click.stop="deleteSelectedFeedback">Yes</v-btn>
                    <v-btn color="primary" text @click.stop="showConfirmDeleteModal = false">No</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

    </v-container>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import SectionHeader from '../header.vue';
import { ErrorResponse, Feedback, PagedDataRep, VueDataTableOptions } from '../rak';
import { Watch } from 'vue-property-decorator';
import restApi from '../rest-api';
import Toaster from '../toaster';
import rakUtil from '../util';

@Component({ components: { SectionHeader } })
export default class FeedbackManager extends Vue {

    selectedFeedback: any[] = [];

    private totalItems: number = 0;

    private items: Feedback[] = [];

    private loading: boolean = true;

    private showConfirmDeleteModal: boolean = false;

    tableOptions: VueDataTableOptions = {
        page: 0,
        itemsPerPage: 20,
        sortBy: [ 'createDate' ],
        sortDesc: [ true ],
        groupBy: [],
        groupDesc: [],
        multiSort: true,
        mustSort: false
    };

    get headers(): any[] /*VTableHeader[]*/ {

        return [
            { text: 'E-mail', value: 'email' },
            { text: 'Title', value: 'title' },
            { text: 'Date', value: 'createDate' }
        ];
    }

    isReplyDisabled(): boolean {
        return this.loading || this.selectedFeedback.length !== 1 || !this.selectedFeedback[0].email;
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

    deleteSelectedFeedback() {
        this.showConfirmDeleteModal = false;
        this.loading = true;
        this.deleteSelectedFeedbackImpl(this.selectedFeedback.slice(), 0);
    }

    private deleteSelectedFeedbackImpl(feedbacks: Feedback[], index: number) {

        const feedback: Feedback = feedbacks[index];

        restApi.deleteFeedback(feedback.id!)
            .then(() => {
                if (index < feedbacks.length - 1) {
                    this.deleteSelectedFeedbackImpl(feedbacks, index + 1);
                }
                else {
                    this.selectedFeedback.length = 0;
                    this.reloadTable();
                    Toaster.success('Feedback successfully deleted');
                }
            })
            .catch((error: ErrorResponse) => {
                this.reloadTable();
                Toaster.error('An error occurred deleting one or more feedback entries');
            });
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
        this.showConfirmDeleteModal = true;
    }

    onReply() {
        const feedback: Feedback = this.selectedFeedback[0];
        rakUtil.programmaticallyClickLink(this.createMailtoUrl(feedback));
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

        return restApi.getFeedback(options.page - 1, 10000, {}, sort)
            .then((pagedData: PagedDataRep<Feedback>) => {
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
}
</script>

<style lang="less">
.feedback-wrapper {

    .feedback-button-section {

        margin-bottom: 0.5rem;

        .feedback-button {
            margin-left: 0.5rem;
            margin-right: 0.5rem;
        }
    }

    .feedback-body {
        margin: 0 3rem;
    }

}
</style>
