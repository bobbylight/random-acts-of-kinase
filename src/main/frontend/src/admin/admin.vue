<template>
    <div class="admin-wrapper">

        <v-fab-transition>
            <v-btn
                v-if="!navDrawerOpen"
                small
                absolute
                dark
                fab
                top
                left
                color="pink"
                @click="navDrawerOpen = true"
                style="top: 1rem"
            >
                <v-icon>chevron_right</v-icon>
            </v-btn>
        </v-fab-transition>

        <v-layout fill-height> <!-- don't wrap or justify-center -->

            <transition name="slide-x-transition" mode="out-in">
                <v-navigation-drawer class="slide-out-admin-options"
                                     :width="380" v-if="navDrawerOpen && this.$store.getters.loggedIn">

                    <v-toolbar flat class="transparent">
                        <v-list class="pt-0 admin-nav-header">
                            <v-list-item class="admin-nav-header-list-item">
                                <v-list-item-action class="admin-menu-icon">
                                    <v-icon>fas fa-cog</v-icon>
                                </v-list-item-action>
                                <v-list-item-content>
                                    Admin Actions
                                </v-list-item-content>
                                <v-list-item-action>
                                    <v-btn icon class="close-nav-drawer-button"
                                           @click.stop="navDrawerOpen = false">
                                        <v-icon>chevron_left</v-icon>
                                    </v-btn>
                                </v-list-item-action>
                            </v-list-item>
                        </v-list>
                    </v-toolbar>

                    <v-list dense class="pt-0">

                        <v-divider></v-divider>

                        <v-list-group
                            no-action
                            v-model="importSubMenuExpanded">

                            <template v-slot:activator>
                                <v-list-item-action class="admin-menu-icon">
                                    <v-icon>fa-upload</v-icon>
                                </v-list-item-action>
                                <v-list-item-content>
                                    <v-list-item-title>Import Data</v-list-item-title>
                                </v-list-item-content>
                            </template>

                            <v-list-item to="import-compounds" class="admin-sub-item">
                                <v-list-item-content>
                                    <v-list-item-title>Import Compounds</v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>

                            <v-list-item to="import-activity-profiles" class="admin-sub-item">
                                <v-list-item-content>
                                    <v-list-item-title>Import Activity Profiles</v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>

                            <v-list-item to="import-kds" class="admin-sub-item">
                                <v-list-item-content>
                                    <v-list-item-title>Import K<sub>d</sub>s</v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>

                            <v-list-item to="import-nanobret" class="admin-sub-item">
                                <v-list-item-content>
                                    <v-list-item-title>Import NanoBRET</v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>

                            <v-list-item to="import-s10s" class="admin-sub-item">
                                <v-list-item-content>
                                    <v-list-item-title>Import s(10)s</v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>
                        </v-list-group>

                        <v-list-item to="blog-manager">
                            <v-list-item-action class="admin-menu-icon">
                                <v-icon>fa-newspaper</v-icon>
                            </v-list-item-action>
                            <v-list-item-content>
                                <v-list-item-title>News Posts</v-list-item-title>
                            </v-list-item-content>
                        </v-list-item>

                        <v-list-item to="stats">
                            <v-list-item-action class="admin-menu-icon">
                                <v-icon>fa-database</v-icon>
                            </v-list-item-action>
                            <v-list-item-content>
                                <v-list-item-title>Missing Data</v-list-item-title>
                            </v-list-item-content>
                        </v-list-item>

                        <v-list-item to="feedback">
                            <v-list-item-action class="admin-menu-icon">
                                <v-icon>fa-comments</v-icon>
                            </v-list-item-action>
                            <v-list-item-content>
                                <v-list-item-title>Feedback</v-list-item-title>
                            </v-list-item-content>
                        </v-list-item>

                        <v-list-item to="audit">
                            <v-list-item-action class="admin-menu-icon">
                                <v-icon>fa-history</v-icon>
                            </v-list-item-action>
                            <v-list-item-content>
                                <v-list-item-title>Audit</v-list-item-title>
                            </v-list-item-content>
                        </v-list-item>

                    </v-list>
<!--                </v-flex>-->
</v-navigation-drawer>
            </transition>

            <v-container grid-list-md fill-height class="page-wrapper">
                <v-layout row wrap fill-height justify-center>

                    <unauthorized v-if="!this.$store.getters.loggedIn"/>

                    <div v-if="this.$store.getters.loggedIn" style="width:100%">
                        <transition name="fade">
                            <keep-alive>
                                <router-view></router-view>
                            </keep-alive>
                        </transition>
                    </div>
                </v-layout>
            </v-container>
        </v-layout>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import unauthorized from './unauthorized.vue';

@Component({ components: { unauthorized } })
export default class AdminHome extends Vue {

    navDrawerOpen: boolean = true;

    importSubMenuExpanded: boolean = true;
}
</script>

<style lang="less">
.admin-wrapper {
    position: relative;
    height: 100%;
    padding-left: 0 !important;
    padding-right: 0 !important;

    .page-wrapper {
        padding: 8px;
    }
    .container {
        padding: 8px;
    }

    .slide-out-admin-options {

        .v-toolbar__content {
            padding: 0;
        }

        .admin-nav-header {
            width: 100%;
            padding: 0 !important;

            .admin-nav-header-list-item {
                padding-right: 5px;
            }
        }

        .admin-menu-icon {
            width: 27px;

            svg {
                margin: 0 auto;
            }
        }

        .admin-sub-item {
            padding-left: 5rem !important;
        }

        a:hover {
            text-decoration: none;
        }
    }
}
</style>
