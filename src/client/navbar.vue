<template>

    <div class="ui top fixed inverted menu">

        <div class="item">
            KINASE!
        </div>

        <a class="item" v-bind:class="{ active: isActiveTab('home') }" v-on:click="setActiveTab('home')">
            Search
        </a>
        <a class="item" v-bind:class="{ active: isActiveTab('about') }" v-on:click="setActiveTab('about')">
            About
        </a>

        <div class="button-section item">
            <div v-for="compound in openCompounds">
                <navbar-pill :label="compound"></navbar-pill>
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
            const tabNameRegex = new RegExp(tabName);
            console.log(tabName + ' -- ' + this.$route.name + ', ' + (this.$route.name && this.$route.name.match(tabNameRegex)));
            return this.$route.name && !!this.$route.name.match(tabNameRegex);
        },

        setActiveTab(tabName) {

            switch (tabName) {

                case 'home':
                default:
                    this.$router.push({ name: 'home' });
                    break;
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

<style>
</style>
