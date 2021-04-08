<template>
    <v-app>

        <lightbox :title="$store.state.lightboxTitle"
                  :image="$store.state.lightboxImage"
                  @hide="onCloseLightbox"></lightbox>

        <navbar></navbar>

        <v-main class="main-content">

            <transition name="fade">
                <keep-alive>
                    <router-view :key="$route.params.id"></router-view>
                </keep-alive>
            </transition>
        </v-main>

        <v-footer class="theme--dark justify-center" height="auto">
                    <div class="copyright">
                        &copy; 2021 <a href="http://sgc-unc.org">SGC-UNC</a>
                    </div>
                    <v-btn class="footer-button theme--dark" icon small @click="viewSource"
                           title="View Source" aria-label="View Source">
                        <v-icon small>fab fa-github</v-icon>
                    </v-btn>
                    <v-btn class="footer-button theme--dark" icon small @click.stop="showAbout = true"
                           title="About" aria-label="About">
                        <v-icon small>fas fa-question-circle</v-icon>
                    </v-btn>
        </v-footer>

        <about-modal :show="showAbout" @close="showAbout = false"></about-modal>
    </v-app>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import Navbar from './navbar.vue';
import restApi from './rest-api';
import { UserRep } from './rak';
import AboutModal from './about-modal.vue';
import Lightbox from './lightbox.vue';

@Component({ components: { Navbar, AboutModal, Lightbox } })
export default class App extends Vue {

    showAbout: boolean = false;

    mounted() {
        restApi.checkAuthentication()
            .then((userInfo: UserRep) => {
                console.log('>>> Check completed, returned: ' + JSON.stringify(userInfo));
                if (userInfo && userInfo.userName) {
                    this.$store.commit('setUser', userInfo.userName);
                }
            });
    }

    private onCloseLightbox() {
        this.$store.commit('setLightboxImage', null);
    }

    private viewSource() {
        window.open('https://github.com/bobbylight/rak', '_blank');
    }
}
</script>

<style lang="less">
.main-content {
    min-height: 800px;
}

.fade-enter-active, .fade-leave-active {
    transition-property: opacity;
    transition-duration: .25s;
}

.fade-enter-active {
    transition-delay: .25s;
}

.fade-enter, .fade-leave-active {
    opacity: 0
}

footer {
    .copyright {

        align-items: center;
        margin-right: 3rem;

        a {
            text-decoration: none;
            color: inherit;
        }
    }

    .footer-button {
        padding-left: 1rem;
        padding-right: 1rem;
    }

    .btn.btn--icon {
        margin-left: 0;
        margin-right: 0;
    }
}
</style>
