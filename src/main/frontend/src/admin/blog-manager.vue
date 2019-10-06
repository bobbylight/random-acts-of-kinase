<template>
    <v-container grid-list-md>
        <v-layout row wrap class="blog-manager-wrapper">

            <section-header>News Posts</section-header>

            <v-flex xs12 class="blog-manager-top-section">

                <p>
                    Create, edit, or delete news posts.
                </p>

                <v-btn color="info" @click="onNewPost">New Post</v-btn>
            </v-flex>

            <v-flex xs12>

                <v-data-table
                    :headers="headers"
                    class="elevation-1"
                    :items="items"
                    :items-per-page="tableOptions.itemsPerPage"
                    :server-items-length="totalItems"
                    :options.sync="tableOptions"
                    :loading="loading"
                    multi-sort
                    :footer-props="{ 'items-per-page-options': [ 20, 50, 100 ] }"
                >

                    <template v-slot:item="{ item }">
                        <blog-manager-post-name-cell
                            :post="item"
                            @deletePost="onDelete($event)"
                            @editPost="onEdit($event)"
                            @postsUpdated="reloadTable"></blog-manager-post-name-cell>
                    </template>
                </v-data-table>
            </v-flex>
        </v-layout>

        <confirm-modal
            :show="showDeleteConfirmation"
            title="Confirm Delete Post"
            :details="confirmDeletePostDetails"
            @confirmResult="onConfirmDeleteModalClosed($event)"
        ></confirm-modal>

        <blog-post-editor-modal
            :show="showBlogEditor"
            :post="blogEditorPost"
            @close="onBlogEditorModalClosed()">
        ></blog-post-editor-modal>

    </v-container>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Watch } from 'vue-property-decorator';
import { BlogPost, ErrorResponse, PagedDataRep, VueDataTableOptions } from '../rak';
import ConfirmModal, { ConfirmResult } from '../confirm-modal.vue';
import SectionHeader from '../header.vue';
import BlogManagerPostNameCell from './blog-manager-post-name-cell.vue';
import restApi from '../rest-api';
import Toaster from '../toaster';
import BlogPostEditorModal from './blog-post-editor-modal.vue';

@Component({ components: { BlogPostEditorModal, SectionHeader, BlogManagerPostNameCell, ConfirmModal } })
export default class BlogManager extends Vue {

    private totalItems: number = 0;

    private items: BlogPost[] = [];

    private loading: boolean = true;

    private showDeleteConfirmation: boolean = false;

    private confirmDeletePostDetails: string = '';

    private postToDelete: BlogPost;

    private showBlogEditor: boolean = false;

    private blogEditorPost: BlogPost | null = null;

    tableOptions: VueDataTableOptions = {
        page: 1,
        itemsPerPage: 10,
        sortBy: [ 'createDate' ],
        sortDesc: [ true ],
        groupBy: [],
        groupDesc: [],
        multiSort: true,
        mustSort: false
    };

    onBlogEditorModalClosed() {
        this.showBlogEditor = false;
        this.reloadTable();
    }

    onConfirmDeleteModalClosed(result: ConfirmResult) {

        this.showDeleteConfirmation = false;

        if (result === 'yes') {
            restApi.deleteBlogPost(this.postToDelete.id!)
                .then(() => {
                    Toaster.success('News post deleted');
                    this.reloadTable();
                })
                .catch((error: ErrorResponse) => {
                    Toaster.error('An error occurred deleting the news post');
                });
        }
    }

    onDelete(post: BlogPost) {
        this.postToDelete = post;
        this.confirmDeletePostDetails = `Are you sure you want to delete the news post "${post.title}"?`;
        this.showDeleteConfirmation = true;
    }

    onEdit(post: BlogPost) {
        this.blogEditorPost = post;
        this.showBlogEditor = true;
    }

    onNewPost() {
        this.blogEditorPost = null;
        this.showBlogEditor = true;
    }

    get headers(): any[] /*VTableHeader[]*/ {

        return [
            { text: 'Title', value: 'title' },
            { text: 'Author', value: 'author', sortable: false },
            { text: 'Date', value: 'createDate' }
        ];
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

        return restApi.getBlogPosts(options.page - 1, 10000, {}, sort)
            .then((pagedData: PagedDataRep<BlogPost>) => {
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
.blog-manager-wrapper {

    .blog-manager-top-section {
        margin-bottom: 0.5rem;
    }
}
</style>
