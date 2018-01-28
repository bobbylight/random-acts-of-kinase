<template>
    <div class="app-wrapper">

        <navbar></navbar>

        <div class="ui main">

            <transition name="fade">
                <keep-alive>
                    <router-view :key="$route.params.id"></router-view>
                </keep-alive>
            </transition>
        </div>

        <div class="ui bottom inverted menu footer">
            <div class="footer-content menu">
                <span class="item">
                    &copy; 2018&nbsp;<a href="http://sgc-unc.org">SGC-UNC</a>
                </span>
                <a class="item" @click="viewSource()" title="View Source" aria-label="View Source">
                    <i class="fa fa-github" aria-hidden="true"></i>
                </a>
                <a class="item" @click="showAbout()" title="About" aria-label="About">
                    <i class="fa fa-question-circle" aria-hidden="true"></i>
                </a>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import $ from 'jquery';
import Navbar from './navbar.vue';
import restApi from './rest-api';
import { UserRep } from './rak';

@Component({ components: { Navbar } })
export default class App extends Vue {

    mounted() {
        restApi.checkAuthentication()
            .then((userInfo: UserRep) => {
                console.log('>>> Check completed, returned: ' + JSON.stringify(userInfo));
                if (userInfo && userInfo.userName) {
                    this.$store.commit('setUser', userInfo.userName);
                }
            });
    }

    private showAbout() {
        $('#aboutModal').modal('show');
    }

    private viewSource() {
        window.open('https://github.com/bobbylight/rak', '_blank');
    }
}
</script>

<style lang="less">
/*.top-padding {*/
    /*background: #f8f8f8;*/
/*}*/
.app-wrapper {

    .ui.main {
        min-height: 800px;
    }
}

.ui.menu.footer {
    border-radius: 0;

    .footer-content {
        margin: 0 auto;
    }
}

.fade-enter-active, .fade-leave-active {
    transition-property: opacity;
    transition-duration: .25s;
}

.fade-enter-active {
    transition-delay: .25s;
}

.fade-enter, .fade-leave-active {
    opacity: 0
}
</style>
