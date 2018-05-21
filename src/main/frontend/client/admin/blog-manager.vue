<template>
    <v-container grid-list-md>
        <v-layout row wrap class="blog-manager-wrapper">

            <section-header>News Posts</section-header>

            <v-flex xs12>

                <div>
                    Create, edit, or delete news posts.
                </div>

                <v-btn color="info" @click="onNewPost">New Post</v-btn>
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
                    :rows-per-page-items='[ 20, 50, 100 ]'
                >

                    <template slot="items" slot-scope="props">
                        <blog-manager-post-name-cell
                            :post="props.item"
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
import { BlogPost, ErrorResponse, PagedDataRep } from '../rak';
import ConfirmModal, { ConfirmResult } from '../confirm-modal.vue';
import SectionHeader from '../header.vue';
import BlogManagerPostNameCell from './blog-manager-post-name-cell.vue';
import restApi from '../rest-api';
import Toaster from '../toaster';
import BlogPostEditorModal from './blog-post-editor-modal.vue';

@Component({ components: { BlogPostEditorModal, SectionHeader, BlogManagerPostNameCell, ConfirmModal } })
export default class BlogManager extends Vue {

    private search: string = '';

    private totalItems: number = 0;

    private items: BlogPost[] = [];

    private loading: boolean = true;

    private showDeleteConfirmation: boolean = false;

    private confirmDeletePostDetails: string = '';

    private postToDelete: BlogPost;

    private showBlogEditor: boolean = false;

    private blogEditorPost: BlogPost | null = null;

    private pagination: any = {
        sortBy: 'createDate',
        descending: true
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

        const { sortBy, descending, page, rowsPerPage } = this.pagination;

        const sort: string = sortBy ? `${sortBy},${descending ? 'desc' : 'asc'}` : '';

        return restApi.getBlogPosts(page - 1, 10000, {}, sort)
            .then((pagedData: PagedDataRep<BlogPost>) => {
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
</style>
