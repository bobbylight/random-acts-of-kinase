<template>
    <v-container grid-list-md>
        <v-layout row wrap class="blog-manager-wrapper">

            <v-flex xs12>

                <v-card class="blog-post-card-padding">

                    <v-card-title primary-title>
                        <div class="title-content">
                            <h3 class="headline">Create New Post</h3>
                        </div>
                    </v-card-title>

                    <v-card-text>

                        <v-flex xs12>
                            <v-text-field type="text" label="Post Title" v-model="title"></v-text-field>
                        </v-flex>

                        <rich-text-editor :emitChangeEvents="true" @change="editorContentChanged"></rich-text-editor>
                    </v-card-text>

                    <v-card-actions>

                        <v-spacer></v-spacer>

                        <v-btn :disabled="isFormIncomplate()" @click="post">
                            Post
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { BlogPost, ErrorResponse } from '../rak';
import RichTextEditor, { ChangeEvent } from './rich-text-editor.vue';
import restApi from '../rest-api';
import Toaster from '../toaster';

@Component({ components: { RichTextEditor } })
export default class BlogManager extends Vue {

    private title: string = '';
    private body: string = '';
    private editorEmpty: boolean = true;

    private editorContentChanged(e: ChangeEvent) {
        this.editorEmpty = e.isEmpty();
        this.body = e.getContent();
        console.log(this.body);
    }

    private isFormIncomplate(): boolean {
        return this.title.length === 0 || this.editorEmpty;
    }

    private post() {

        const post: BlogPost = {
            title: this.title,
            body: this.body
        };

        restApi.saveBlogPost(post)
            .then(() => {
                alert('Success!');
            })
            .catch((error: ErrorResponse) => {
                Toaster.error(error.message);
            });
    }
}
</script>

<style lang="less">
</style>
