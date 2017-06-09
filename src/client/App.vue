<template>
    <div class="app-wrapper">

        <navbar></navbar>

        <div class="ui main">

            <router-view></router-view>
        </div>
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
/*.top-padding {*/
    /*background: #f8f8f8;*/
/*}*/
.app-wrapper {
    margin-top: 4em;
}
</style>
