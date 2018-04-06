<template>
    <v-container fluid grid-list-md>

        <v-layout row wrap class="blog-wrapper">
            <v-flex xs12>

                <h1>News</h1>

                <div v-if="postCount === -1">
                    Loading...
                </div>

                <div v-if="postCount === 0">
                    No posts
                </div>

                <div v-if="postCount > 0">

                    <blog-post-widget v-for="post in blogPosts" :post="post"></blog-post-widget>

                    <div class="blog-page-footer">
                        Showing {{postStart}} - {{postEnd}} of {{postTotal}}
                    </div>
                </div>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import BlogPostWidget from './blog-post.vue';
import { BlogPost, PagedDataRep } from './rak';
import restApi from './rest-api';

@Component({ components: { BlogPostWidget } })
export default class Blog extends Vue {

    private postCount: number = -1;
    private postStart: number = 0;
    private postEnd: number = 0;
    private postTotal: number = 0;

    private blogPosts: any = [];

    created() {

        restApi.getBlogPosts(0, 100)
            .then((posts: PagedDataRep<BlogPost[]>) => {
                this.blogPosts = posts.data;
                this.postCount = posts.count;
                this.postStart = posts.start + 1;
                this.postEnd = this.postStart + this.postCount - 1;
                this.postTotal = posts.total;
            })
            .catch(() => {
                this.postCount = 0;
            });
    }
}
</script>

<style lang="less">
.blog-wrapper {

    padding: 2rem 6rem;

    .blog-page-footer {
        margin-top: 1rem;
        text-align: right;
    }
}
</style>
