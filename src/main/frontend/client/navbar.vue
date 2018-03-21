<template>

    <v-toolbar app dark>

        <v-toolbar-title @click="onReset()">
            <img src="/img/molecule-white.svg" width="50" height="50"> <!--KIANSE-->
        </v-toolbar-title>

        <v-btn v-bind:class="{ active: isActiveTab('/') }" @click="setActiveTab('home')">
            Search
        </v-btn>

        <v-btn class="item" v-bind:class="{ active: isActiveTab('/admin') }"
               @click="setActiveTab('admin')" v-if="$store.getters.loggedIn">
            Admin
        </v-btn>

        <div class="button-section item">
            <div v-for="compound in openCompounds">
                <navbar-pill :compound="compound" v-on:close="close($event)"></navbar-pill>
            </div>
        </div>

        <v-spacer></v-spacer>

        <v-btn icon class="item" @click="showLogin = true" title="Login" aria-label="Login"
                v-if="!$store.getters.loggedIn">
            <i class="fa fa-user" aria-hidden="true"></i>
        </v-btn>
        <v-btn icon title="Log out" aria-label="Log out" v-if="$store.getters.loggedIn">
            <i class="fa fa-user" aria-hidden="true"></i><span class="user-name">{{$store.state.user}}</span>
            <div class="menu">
                <div class="item">
                    <span class="text" @click="logout">Log Out</span>
                </div>
            </div>
        </v-btn>
        <v-btn icon @click="newComment()" title="Comment" aria-label="Comment">
            <i class="fa fa-comment" aria-hidden="true"></i>
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

    updated() {
        if (this.$store.getters.loggedIn) {
            $(this.$el).find('.ui.dropdown').dropdown();
        }
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
.ui.top.menu {
    height: 4rem;
    border-radius: 0;
    margin-bottom: 0;
}
.ui.menu a.item {
    transition: color .5s ease,
    background-color .5s ease;

    .user-name {
        margin-left: 1rem;
        font-size: initial;
    }

    .menu {
        font-size: initial;
    }
}

.ui.menu .button-section.item:before {
    width: 0; // Turn off right-aligned 1-pixel border
}

.right.menu {
    font-size: 1.5rem;

    .item {
        padding: 0 1rem;
    }
}
</style>
