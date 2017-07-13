<template>

    <div class="ui top inverted menu">

        <div class="item">
            <!--<img src="img/molecule.svg" width="50" height="50">-->
            KINASE!!!!1111
        </div>

        <a class="item" v-bind:class="{ active: isActiveTab('/') }" v-on:click="setActiveTab('home')">
            Search
        </a>

        <div class="button-section item">
            <div v-for="compound in openCompounds">
                <navbar-pill :compound="compound" v-on:close="close($event)"></navbar-pill>
            </div>
        </div>

        <div class="right menu">
            <a class="item" v-on:click="showAbout()" title="About" aria-label="About">
                <i class="fa fa-comment" aria-hidden="true"></i>
            </a>
            <a class="item" v-on:click="showAbout()" title="About" aria-label="About">
                <i class="fa fa-question-circle" aria-hidden="true"></i>
            </a>
        </div>

        <about-modal id="aboutModal"></about-modal>
    </div>

</template>

<script>
import NavbarPill from './navbar-pill.vue';
import AboutModal from './about-modal.vue';

export default {
    name: 'navbar',
    components: {
        NavbarPill,
        AboutModal
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
        },

        showAbout() {
            $('#aboutModal').modal('show');
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
