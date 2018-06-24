<template>

    <v-card class="blog-post-card">

        <v-card-title primary-title>

            <div class="title-content">
                <h3 class="headline">
                    {{post.title}}
                </h3>
            </div>
        </v-card-title>

        <!-- Quill classes specified to force rendering closer to that in quill itself -->
        <v-card-text class="ql-editor" v-html="body"></v-card-text>

        <v-card-actions class="blog-card-actions">
            <div class="grey--text blog-post-viewCount">
                0 views
            </div>
            <v-spacer></v-spacer>
            <div class="grey--text blog-post-date">
                Posted by
                <user-badge user="admin"></user-badge>
                on
                {{dateString}}
            </div>
        </v-card-actions>
    </v-card>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';
import deltaToHtml from './delta-to-html';
import UserBadge from './user-badge.vue';
import { BlogPost } from './rak';
import rakUtil from './util';

@Component({ components: { UserBadge } })
export default class BlogPostWidget extends Vue {

    @Prop({ required: true })
    private post: BlogPost;

    get dateString(): string {
        return rakUtil.getDisplayDate(this.post.createDate!);
    }

    get body(): string {
        return deltaToHtml.convert(JSON.parse(this.post.body));
    }
}
</script>

<style lang="less">
@action-color: lightgray;

.blog-card-actions {

    border-top: 1px solid @action-color;
    margin-left: 16px; // Matches margin on v-card-title/text
    margin-right: 16px; // Matches margin on v-card-title/text

    font-style: italic;
}
</style>
