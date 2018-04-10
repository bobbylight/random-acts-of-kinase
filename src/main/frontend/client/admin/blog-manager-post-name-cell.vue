<template>
    <tr class="blog-manager-row">
        <td class="blog-manager-cell">
            <div class="subheading">
                {{post.title}}
            </div>
            <div class="blog-manager-post-options">
                Edit | View | Delete
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
import { BlogPost } from '../rak';

@Component
export default class BlogManagerPostNameCell extends Vue {

    @Prop({ required: true })
    private post: BlogPost;

    get beautifiedCreateDate(): string {
        return new Date(this.post.createDate!).toLocaleDateString();
    }
}
</script>

<style lang="less">

.blog-manager-post-options {
    visibility: hidden;
}
tr:hover .blog-manager-post-options {
    visibility: visible;
}

.blog-manager-cell {
    padding: 1rem !important;
}
</style>
