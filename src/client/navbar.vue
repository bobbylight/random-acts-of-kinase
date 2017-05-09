<template>

    <div class="ui top fixed inverted menu">

        <div class="item">
            KINASE!
        </div>

        <a class="item" v-bind:class="{ active: 'search' === activeTab}" v-on:click="setActiveTab('search')">
            Search
        </a>
        <a class="item" v-bind:class="{ active: 'messages' === activeTab}" v-on:click="setActiveTab('messages')">
            Messages
        </a>
        <a class="item" v-bind:class="{ active: 'friends' === activeTab}" v-on:click="setActiveTab('friends')">
            Friends
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
            activeTab: 'search',
            openCompounds: []
        };
    },
    methods: {

        setActiveTab(tabName) {
            this.activeTab = tabName;
        },

        foo() {
            console.log('foo foo baby');
        },

        createCompoundTab(compound) {
            new NavbarPill({
                el: this.$el.querySelector('.button-section'),
                parent: this
            });
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

    /* pill styles are essentially copied from ui inverted menu */
    .ui.menu .ui.button.pill {
        background: rgba(255, 255, 255, 0.08);
        color: rgba(255,255,255,.9);
        font-weight: 400;
        padding-top: 0.5rem;
        padding-bottom: 0.5rem;
    }
    .ui.menu .ui.button.pill:hover {
        background: rgba(255, 255, 255, 0.15);
        color: #fff;
    }
</style>
