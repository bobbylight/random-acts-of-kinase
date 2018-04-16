<template>
    <v-dialog v-model="visible" max-width="800px" @keydown.esc="onCancel">
        <v-card>

            <v-card-title class="headline">{{modalTitle}}</v-card-title>

            <v-card-text>

                <v-flex xs12>
                    <v-text-field type="text" label="Post Title" v-model="title" ref="titleField"></v-text-field>
                </v-flex>

                <rich-text-editor
                    :initial-content="body"
                    :emitChangeEvents="true"
                    @change="editorContentChanged"></rich-text-editor>
            </v-card-text>

            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn :disabled="!dirty || isFormIncomplate()" @click="onCreateOrUpdatePost">
                    {{submitButtonLabel}}
                </v-btn>
                <v-btn @click="onCancel">
                    Cancel
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop, Watch } from 'vue-property-decorator';
import { BlogPost, ErrorResponse } from '../rak';
import RichTextEditor, { ChangeEvent } from './rich-text-editor.vue';
import restApi from '../rest-api';
import Toaster from '../toaster';

const CLOSE: string = 'close';

@Component({ components: { RichTextEditor } })
export default class BlogPostEditorModal extends Vue {

    @Prop({ required: true })
    private show: boolean;

    @Prop()
    private post: BlogPost;

    private modalTitle: string = '';
    private title: string = '';
    private body: any = {};
    private wipBody: any | null = null;
    private editorEmpty: boolean = true;
    private dirty: boolean = false;
    private submitButtonLabel: string = '';

    /**
     * Update labels and field values based on whether this is a new post or an edit.
     */
    private refresh() {
        if (this.post) {
            this.modalTitle = 'Edit News Post';
            this.title = this.post.title;
            this.body = JSON.parse(this.post.body);
            this.submitButtonLabel = 'Update';
        }
        else {
            this.modalTitle = 'Create News Post';
            this.title = '';
            this.body = {};
            this.submitButtonLabel = 'Post';
        }
        this.dirty = false;
    }

    get visible() {
        return this.show;
    }

    set visible(newValue: boolean) {
        if (!newValue) {
            this.onCancel();
        }
    }

    private editorContentChanged(e: ChangeEvent) {
        this.dirty = true;
        this.editorEmpty = e.isEmpty();
        this.wipBody = e.getContent();
        console.log('New editor content: ' + JSON.stringify(this.wipBody));
    }

    private isFormIncomplate(): boolean {
        return this.title.length === 0 || this.editorEmpty;
    }

    private onCancel() {
        this.$emit(CLOSE);
    }

    private onCreateOrUpdatePost() {

        const newPostInfo: BlogPost = {
            title: this.title,
            body: JSON.stringify(this.wipBody || this.body)
        };

        let updating: boolean = false;
        if (this.post) { // Editing an existing post
            newPostInfo.id = this.post.id;
            updating = true;
        }

        let promise: Promise<BlogPost> | null = null;
        let successMessage: string;
        if (updating) {
            promise = restApi.updateBlogPost(newPostInfo);
            successMessage = 'News post successfully updated.';
        }
        else {
            promise = restApi.saveBlogPost(newPostInfo);
            successMessage = 'News post successfully created.';
        }

        promise!.then(() => {
                this.$emit(CLOSE);
                Toaster.success(successMessage);
            })
            .catch((error: ErrorResponse) => {
                Toaster.error(error.message);
            });
    }

    /**
     * When this modal is told to show itself, focus the title field.
     *
     * @param {boolean} newValue Whether to show or hide ourselves.
     */
    @Watch('show')
    private onShowChanged(newValue: boolean) {
        if (newValue) {
            this.refresh();
            this.$nextTick(() => {
                (this.$refs.titleField as HTMLInputElement).focus();
            });
        }
    }
}
</script>

<style lang="less">
</style>
