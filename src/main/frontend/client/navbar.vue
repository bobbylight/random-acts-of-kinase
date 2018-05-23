<template>

    <v-toolbar app absolute dark>

        <v-toolbar-title class="toolbar-title-fix" @click="setActiveTab('home')">
            <img src="/img/molecule-white.svg" width="50" height="50" class="navbar-image">
        </v-toolbar-title>

        <v-toolbar-items>
            <v-btn flat v-bind:class="{ 'active-toolbar-item': isActiveTab('/search') }" @click="setActiveTab('search')">
                Search
            </v-btn>

            <v-btn flat v-bind:class="{ 'active-toolbar-item': isActiveTab('/blog') }" @click="setActiveTab('blog')">
                News
            </v-btn>

            <v-btn flat v-bind:class="{ 'active-toolbar-item': isActiveTab('/admin') }"
                   @click="setActiveTab('import-compounds')" v-if="$store.getters.loggedIn">
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
        <v-btn flat icon large @click="newComment()" title="Comment" aria-label="Comment">
            <v-icon>comment</v-icon>
        </v-btn>

        <login-modal :show="showLogin" @close="showLogin = false"></login-modal>
    </v-toolbar>

</template>

<script lang="ts">
import Vue from 'vue';
import { Route } from 'vue-router';
import { Watch } from 'vue-property-decorator';
import Component from 'vue-class-component';
import NavbarPill from './navbar-pill.vue';
import LoginModal from './login-modal.vue';
import RakUtil from './util';
import restApi from './rest-api';
import Toaster from './toaster';

@Component({ components: { NavbarPill, LoginModal } })
export default class Navbar extends Vue {

    openCompounds: string[] = [];
    private showLogin: boolean = false;

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

    private newComment() {
    }

    @Watch('$route')
    private onRouteChanged(to: Route, from: Route) {
        if (to.path.match(/\/compound\/\w+/)) {
            // tslint:disable:no-string-literal
            const compound: string = to.params['id'];
            console.log('Compound clicked; adding pill for it if one doesn\'t yet exist: ' + compound);
            if (this.openCompounds.indexOf(compound) === -1) {
                this.openCompounds.push(compound);
            }
        }
    }
}
</script>

<style lang="less">
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
</style>
