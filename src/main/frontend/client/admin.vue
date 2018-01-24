<template>
    <div class="ui container admin-main">
        There's nothing in the admin panel yet.
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';

@Component
export default class AdminHome extends Vue {
}
</script>

<style lang="less">
.admin-main {
    padding-top: 1rem; // Matches search.vue
}
</style>
