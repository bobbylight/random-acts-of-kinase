<template>

    <div class="ui top fixed inverted menu">

        <div class="item">
            KINASE!
        </div>

        <a class="item" v-bind:class="{ active: isActiveTab('/') }" v-on:click="setActiveTab('home')">
            Search
        </a>
        <a class="item" v-bind:class="{ active: isActiveTab('about') }" v-on:click="setActiveTab('about')">
            About
        </a>

        <div class="button-section item">
            <div v-for="compound in openCompounds">
                <navbar-pill :compound="compound" v-on:close="close($event)"></navbar-pill>
            </div>
        </div>
    </div>

</template>

<script>
import NavbarPill from './navbar-pill.vue';

export default {
    name: 'navbar',
    components: {
        NavbarPill
    },
    data: () => {
        return {
            activeTab: 'home',
            openCompounds: []
        };
    },
    methods: {

        isActiveTab(tabName) {
            const tabNameRegex = new RegExp(tabName + '$');
            console.log(tabName + ' -- ' + this.$route.fullPath + ', ' + (this.$route.fullPath && this.$route.fullPath.match(tabNameRegex)));
            return this.$route.fullPath && !!this.$route.fullPath.match(tabNameRegex);
        },

        setActiveTab(tabName) {

            switch (tabName) {

                case 'home':
                default:
                    this.$router.push({ name: 'home' });
                    break;
            }
        },

        close($event) {
            const index = this.openCompounds.indexOf($event);
            if (index > -1) {
                this.openCompounds.splice(index, 1);
            }
            const tabNameRegex = new RegExp($event + '$');
            if (this.$route.fullPath && !!this.$route.fullPath.match(tabNameRegex)) {
                console.log('Going back');
                this.$router.back();
            }
        }
    },
    watch: {
        '$route' (to, from) {
            if (to.path.match(/\/compound\/\w+/)) {
                const compound = to.params.id;
                console.log('Compound clicked; adding pill for it if one doesn\'t yet exist: ' + compound);
                if (this.openCompounds.indexOf(compound) === -1) {
                    this.openCompounds.push(compound);
                }
            }
        }
    }
}
</script>

<style lang="less">
.ui.menu a.item {
    transition: color .5s ease,
    background-color .5s ease;
}
</style>
