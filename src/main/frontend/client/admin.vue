<template>
    <div class="ui container admin-main">

        <div class="no-access" v-if="!this.$store.getters.loggedIn">

            <h1>You don't have access to this information :(</h1>

            <router-link :to="{ name: 'home' }">Get out of here</router-link>
        </div>

        <div v-if="this.$store.getters.loggedIn">
            There's nothing in the admin panel yet.
        </div>
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
