<template>
    <v-container grid-list-md>
        <v-layout row wrap class="blog-manager-wrapper py-5">

            <div class="headline pb-2 primary--text">Blog Manager</div>

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

                    <div class="editor"></div>
                </v-card-text>

                <v-card-actions>
                </v-card-actions>
            </v-card>
        </v-layout>
    </v-container>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';
import Quill from 'quill';
import { BlogPost } from '../rak';

@Component({ components: { } })
export default class BlogManager extends Vue {

    private editor: Quill;

    mounted() {

        const toolbarOptions: any[] = [
            [ 'bold', 'italic', 'underline' ],
            [ { color: [] }, { background: [] } ], // Empty arrays => takes defaults from theme
            [ 'link', 'image' ],
            [ { align: [] } ],
            [ { size: [ 'small', false, 'large', 'huge' ] } ],
            [ { header: [ 1, 2, 3, 4, 5, 6, false ] } ],
            [ { font: [] } ],
            [ 'clean' ], // Remove formatting
        ];

        const elem: Element = this.$el.querySelector('.editor');
        this.editor = new Quill(elem, {
            modules: { toolbar: toolbarOptions },
            theme: 'snow',
            placeholder: 'Enter your post here!'
        });
    }
}
</script>

<style lang="less">
</style>
