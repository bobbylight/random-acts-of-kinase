<template>

    <v-toolbar app dark>

        <v-toolbar-title @click="onReset()" class="toolbar-title-fix">
            <img src="/img/molecule-white.svg" width="50" height="50"> <!--KIANSE-->
        </v-toolbar-title>

        <v-toolbar-items>
            <v-btn flat v-bind:class="{ active: isActiveTab('/') }" @click="setActiveTab('home')">
                Search
            </v-btn>

            <v-btn flat v-bind:class="{ active: isActiveTab('/admin') }"
                   @click="setActiveTab('admin')" v-if="$store.getters.loggedIn">
                Admin
            </v-btn>
        </v-toolbar-items>

        <div class="button-section">
            <div v-for="compound in openCompounds">
                <navbar-pill :compound="compound" v-on:close="close($event)"></navbar-pill>
            </div>
        </div>

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
import $ from 'jquery';
import NavbarPill from './navbar-pill.vue';
import LoginModal from './login-modal.vue';
import restApi from './rest-api';

@Component({ components: { NavbarPill, LoginModal } })
export default class Navbar extends Vue {

    openCompounds: string[] = [];
    private showLogin: boolean = false;

    private isActiveTab(tabName: string): boolean {
        const tabNameRegex: RegExp = new RegExp(tabName + '$');
        console.log(tabName + ' -- ' + this.$route.fullPath + ', ' + (this.$route.fullPath && this.$route.fullPath.match(tabNameRegex)));
        return !!this.$route.fullPath && !!this.$route.fullPath.match(tabNameRegex);
    }

    private setActiveTab(tabName: string) {

        switch (tabName) {

            case 'home':
            default:
                this.$router.push({ name: 'home' });
                break;

            case 'admin':
                this.$router.push({ name: 'admin' });
                break;
        }
    }

    private close($event: string) {
        const index: number = this.openCompounds.indexOf($event);
        if (index > -1) {
            this.openCompounds.splice(index, 1);
        }
        const tabNameRegex: RegExp = new RegExp($event + '$');
        if (this.$route.fullPath && !!this.$route.fullPath.match(tabNameRegex)) {
            console.log('Going back');
            this.$router.back();
        }
    }

    private logout() {
        restApi.logout()
            .then(() => {
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
}
.button-section {
    display: flex;
}
</style>
