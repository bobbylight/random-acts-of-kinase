<template>
    <div class="ui main text container">

        <navbar></navbar>

        <router-view></router-view>
    </div>
</template>

<script>

import navbar from './navbar.vue';

export default {
    name: 'app',
    components: {
        navbar
    }
};
</script>

<style lang="less">
.main.container {
    margin-top: 7em;
}
</style>
