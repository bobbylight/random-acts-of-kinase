<template>

    <v-card class="blog-post-card">

        <v-card-title primary-title>
            <div class="title-content">
                <h3 class="headline">{{post.title}}</h3>
            </div>
        </v-card-title>

        <v-card-text v-html="body"></v-card-text>

        <v-card-actions class="blog-card-actions">
            <div class="blog-post-viewCount">
                0 views
            </div>
            <v-spacer></v-spacer>
            <div class="blog-post-date">
                {{dateString}}
            </div>
        </v-card-actions>
    </v-card>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';
import marked from 'marked';
import { BlogPost } from './rak';

@Component({ components: { } })
export default class BlogPostWidget extends Vue {

    @Prop({ required: true })
    private post: BlogPost;

    get dateString() {
        return new Date(this.post.createDate!).toLocaleDateString();
    }

    get body() {

        const markdown: string = this.post.body.replace(/\\n/g, '\n');

        // IntelliJ red-squiggles this, but tsc is fine with it
        return marked(markdown);
    }
}
</script>

<style lang="less">
@action-color: lightgray;

.blog-card-actions {

    border-top: 1px solid @action-color;
    margin-left: 16px; // Matches margin on v-card-title/text
    margin-right: 16px; // Matches margin on v-card-title/text

    color: @action-color;
    font-style: italic;
}
</style>
