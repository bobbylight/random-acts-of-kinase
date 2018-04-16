<template>
    <tr class="blog-manager-row">
        <td class="blog-manager-cell">
            <div class="subheading">
                <a @click="editPost(post.id)">
                    {{post.title}}
                </a>
            </div>
            <div class="blog-manager-post-options">
                <a class="blog-action" @click="editPost(post.id)">Edit</a> |
                <a class="blog-action" @click="viewPost(post.id)">View</a> |
                <a class="blog-action" @click="deletePost(post.id)">Delete</a>
            </div>
        </td>
        <td class="blog-manager-cell">
            {{post.author}}
        </td>
        <td class="blog-manager-cell">
            {{beautifiedCreateDate}}
        </td>
    </tr>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';
import ConfirmModal from '../confirm-modal.vue';
import { BlogPost, ErrorResponse } from '../rak';

@Component({ components: { ConfirmModal } })
export default class BlogManagerPostNameCell extends Vue {

    @Prop({ required: true })
    private post: BlogPost;

    deletePost(postId: number) {
        this.$emit('deletePost', this.post);
    }

    editPost(postId: number) {
        this.$emit('editPost', this.post);
    }

    get beautifiedCreateDate(): string {
        return new Date(this.post.createDate!).toLocaleDateString();
    }

    viewPost(postId: number) {
        alert('Not yet implemented');
    }
}
</script>

<style lang="less">
@import "../../styles/app-variables";

.blog-manager-post-options {
    visibility: hidden;
    opacity: 0;
    transition: opacity @transition-time;
}
tr:hover .blog-manager-post-options {
    visibility: visible;
    opacity: 1;
}

.blog-manager-cell {
    padding: 1rem !important;

    .blog-action {

        color: #757575;
        transition: color @transition-time;

        &:hover {
            color: #212121;
        }
    }
}
</style>
