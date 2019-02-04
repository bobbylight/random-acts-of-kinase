<template>

    <v-toolbar app absolute dark class="rak-navbar">

        <v-toolbar-title class="toolbar-title-fix" @click="setActiveTab('home')">
            <img src="/img/molecule-white.svg" width="50" height="50" class="navbar-image">
        </v-toolbar-title>

        <v-toolbar-items>
            <v-btn flat v-bind:class="{ 'active-toolbar-item': isActiveTab('/search') }" @click="setActiveTab('search')">
                Search
            </v-btn>

            <v-btn flat v-bind:class="{ 'active-toolbar-item': isActiveTab('/nanoBretSearch') }"
                   @click="setActiveTab('nanoBretSearch')" v-if="$store.getters.loggedIn">
                NanoBRET
            </v-btn>

            <v-btn flat v-bind:class="{ 'active-toolbar-item': isActiveTab('/blog') }" @click="setActiveTab('blog')">
                News
            </v-btn>

            <v-btn flat v-bind:class="{ 'active-toolbar-item': isActiveTab('/network') }" @click="setActiveTab('network')">
                Network
            </v-btn>

            <v-btn flat v-bind:class="{ 'active-toolbar-item': isActiveTab('/admin') }"
                   @click="setActiveTab($store.state.lastAdminRouteName)" v-if="$store.getters.loggedIn">
                Admin
            </v-btn>

            <navbar-pill v-for="compound in openCompounds" :key="compound"
                         :compound="compound" @close="close($event)"></navbar-pill>
        </v-toolbar-items>

        <v-spacer></v-spacer>

        <span v-if="$store.getters.loggedIn" class="user-name">{{$store.state.user}}</span>
        <v-btn flat icon large @click="showLogin = true" title="Login" aria-label="Login"
                v-if="!$store.getters.loggedIn">
            <v-icon>fa-user</v-icon>
        </v-btn>
        <v-menu bottom left v-if="$store.getters.loggedIn">
            <v-btn flat icon large slot="activator">
                <v-icon>fa-user</v-icon>
            </v-btn>
            <v-list>
                <v-list-tile @click="logout">
                    <v-list-tile-title>Log Out</v-list-tile-title>
                </v-list-tile>
            </v-list>
        </v-menu>
        <v-btn flat icon large @click="showPartners" title="Partners" aria-label="Partners">
            <v-icon>fa-handshake</v-icon>
        </v-btn>
        <v-btn flat icon large @click="showFeedback = true" title="Feedback" aria-label="Feedback">
            <v-icon>fa-comments</v-icon>
        </v-btn>

        <login-modal :show="showLogin" @close="showLogin = false"></login-modal>
        <feedback-modal :show="showFeedback" @close="showFeedback = false"></feedback-modal>
    </v-toolbar>

</template>

<script lang="ts">
import Vue from 'vue';
import { Route } from 'vue-router';
import { Watch } from 'vue-property-decorator';
import Component from 'vue-class-component';
import NavbarPill from './navbar-pill.vue';
import LoginModal from './login-modal.vue';
import FeedbackModal from './feedback-modal.vue';
import RakUtil from './util';
import restApi from './rest-api';
import Toaster from './toaster';

@Component({ components: { NavbarPill, LoginModal, FeedbackModal } })
export default class Navbar extends Vue {

    openCompounds: string[] = [];
    private showLogin: boolean = false;
    private showFeedback: boolean = false;

    private isActiveTab(tabName: string): boolean {
        return RakUtil.isActiveTab(this.$route, tabName);
    }

    private setActiveTab(tabName: string) {
        this.$router.push({ name: tabName });
    }

    private close($event: string) {
        const index: number = this.openCompounds.indexOf($event);
        if (index > -1) {
            this.openCompounds.splice(index, 1);
        }
        const tabNameRegex: RegExp = new RegExp($event + '$');
        if (this.$route.fullPath && !!decodeURIComponent(this.$route.fullPath).match(tabNameRegex)) {
            console.log('Going back');
            this.$router.back();
        }
    }

    created() {

        // If the user bookmarked a compound, be sure it has an open tab
        this.possiblyOpenCompoundTab(this.$route);
    }

    private logout() {
        restApi.logout()
            .then(() => {

                Toaster.success(`See you next time, ${this.$store.state.user}!`);

                // TODO: This flashes the "no access" stuff on the admin tab for a second.
                // Conver to an action to fix this
                if (this.$router.currentRoute.name !== 'home') {
                    this.$router.push({ name: 'home' });
                }
                this.$store.commit('setUser', null);
            });
    }

    @Watch('$route')
    private onRouteChanged(to: Route, from: Route) {
        this.possiblyOpenCompoundTab(to);
    }

    private possiblyOpenCompoundTab(route: Route) {
        if (route.path.match(/\/compound\/[\w()]+/)) {
            // tslint:disable:no-string-literal
            const compound: string = route.params['id'];
            console.log(`Compound opened; adding pill for it if one doesn't yet exist: ${compound}`);
            if (this.openCompounds.indexOf(compound) === -1) {
                this.openCompounds.push(compound);
            }
        }
    }

    private showPartners() {
        this.$router.push({ name: 'partners' });
    }
}
</script>

<style lang="less">
.rak-navbar {
    .toolbar-title-fix {
        margin-right: 16px;
        cursor: pointer;
    }

    .navbar-image {
        vertical-align: middle;
    }

    .active-toolbar-item {
        background: rgba(255, 255, 255, 0.15) !important;
        color: #fff;
    }
}
</style>
