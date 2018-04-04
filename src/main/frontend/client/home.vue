<template>
    <div>

        <div class="parallax hidden">
            <h1 class="display-3 words-in-image">We Are Open Science</h1>
        </div>

        <v-layout row wrap justify-center class="home-text-section">
            <v-flex xs8 lg6>
                <h1>Advancing Drug Discovery Through Open Collaboration</h1>
                <div class="pt-3">
                    The SGC-UNC team of scientists is dedicated to discovering and openly sharing selective, small molecule
                    inhibitors of protein kinases. Their work aims to expand drug discovery across this important class of
                    disease-relevant proteins and help speed the creation of new medicines for patients.
                </div>
                <div class="pt-3">
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore
                    et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut
                    aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
                    cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in
                    culpa qui officia deserunt mollit anim id est laborum.
                </div>
            </v-flex>
        </v-layout>

        <v-layout row wrap justify-center grey lighten-3 class="home-text-section">
            <v-flex xs8 lg6>
                <h1>About This Tool</h1>
                <div class="pt-3">
                    Random Acts of Kinase allows you to...
                </div>
            </v-flex>
        </v-layout>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
@Component
export default class Home extends Vue {

    created() {
        setTimeout(() => {
            document.getElementsByClassName('parallax')[0].classList.remove('hidden');
        }, 100);
    }
}
</script>

<style lang="less">

.home-text-section {
    padding: 4rem 0;
}

.parallax {
    padding-top: 2rem;
    transition: opacity 6s;
    background-image: url(/img/drew-hays-206414-unsplash.jpg);
    min-height: 600px;

    background-attachment: fixed;
    background-position: center;
    background-repeat: no-repeat;
    background-size: cover;

    .words-in-image {
        color: white;
        margin: 3rem 0 0 3rem;
    }

    &.hidden {
        opacity: 0;
    }
}
</style>
