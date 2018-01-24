<template>

    <div class="ui top inverted menu">

        <div class="item">
            <img src="/img/molecule-white.svg" width="50" height="50">
            <!--KINASE-->
        </div>

        <a class="item" v-bind:class="{ active: isActiveTab('/') }" v-on:click="setActiveTab('home')">
            Search
        </a>

        <a class="item" v-bind:class="{ active: isActiveTab('/admin') }" v-on:click="setActiveTab('admin')" v-if="$store.getters.loggedIn">
            Admin
        </a>

        <div class="button-section item">
            <div v-for="compound in openCompounds">
                <navbar-pill :compound="compound" v-on:close="close($event)"></navbar-pill>
            </div>
        </div>

        <div class="right menu">
            <a class="item" v-on:click="login()" title="Login" aria-label="Login" v-if="!$store.getters.loggedIn">
                <i class="fa fa-user" aria-hidden="true"></i>
            </a>
            <a class="item" v-on:click="logout()" title="Login" aria-label="Log out" v-if="$store.getters.loggedIn">
                <i class="fa fa-user" aria-hidden="true"></i> {{$store.state.user}}
            </a>
            <a class="item" v-on:click="showAbout()" title="About" aria-label="About">
                <i class="fa fa-comment" aria-hidden="true"></i>
            </a>
            <a class="item" v-on:click="showAbout()" title="About" aria-label="About">
                <i class="fa fa-question-circle" aria-hidden="true"></i>
            </a>
        </div>

        <about-modal id="aboutModal"></about-modal>
        <login-modal :visible="loginModalVisible" @hidden="loginModalHidden"></login-modal>
    </div>

</template>

<script lang="ts">
import Vue from 'vue';
import { Watch } from 'vue-property-decorator';
import Component from 'vue-class-component';
import NavbarPill from './navbar-pill.vue';
import AboutModal from './about-modal.vue';
import LoginModal from './login-modal.vue';
import $ from 'jquery';
import { Route } from 'vue-router';

@Component({ components: { NavbarPill, AboutModal, LoginModal } })
export default class Navbar extends Vue {

    openCompounds: string[] = [];
    private loginModalVisible: boolean = false;

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

    private login() {
        this.loginModalVisible = true;
    }

    private loginModalHidden() {
        this.loginModalVisible = false;
    }

    private showAbout() {
        $('#aboutModal').modal('show');
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
