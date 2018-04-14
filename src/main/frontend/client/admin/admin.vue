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

        <v-layout fill-height justify-center>

            <v-flex xs3 fill-height v-if="navDrawerOpen && this.$store.getters.loggedIn"
                    elevation-1 class="slide-out-admin-options" style="position: relative">

                <v-toolbar flat class="transparent">
                    <v-list class="pt-0">
                        <v-list-tile avatar>
                            <v-list-tile-action>
                                <v-icon>fa-cog</v-icon>
                            </v-list-tile-action>
                            <v-list-tile-content>
                                Admin Actions
                            </v-list-tile-content>
                            <v-list-tile-action>
                                <v-btn icon @click.stop="navDrawerOpen = false">
                                    <v-icon>chevron_left</v-icon>
                                </v-btn>
                            </v-list-tile-action>
                        </v-list-tile>
                    </v-list>
                </v-toolbar>

                <v-list dense class="pt-0">

                    <v-divider></v-divider>

                    <v-list-tile to="stats">
                        <v-list-tile-action>
                            <v-icon>fa-database</v-icon>
                        </v-list-tile-action>
                        <v-list-tile-content>
                            <v-list-tile-title>Missing Data</v-list-tile-title>
                        </v-list-tile-content>
                    </v-list-tile>

                    <v-list-tile to="blog-new-post">
                        <v-list-tile-action>
                            <v-icon>mode_edit</v-icon>
                        </v-list-tile-action>
                        <v-list-tile-content>
                            <v-list-tile-title>New News Post</v-list-tile-title>
                        </v-list-tile-content>
                    </v-list-tile>

                    <v-list-tile to="blog-manager">
                        <v-list-tile-action>
                            <v-icon>fa-newspaper-o</v-icon>
                        </v-list-tile-action>
                        <v-list-tile-content>
                            <v-list-tile-title>Manage News Posts</v-list-tile-title>
                        </v-list-tile-content>
                    </v-list-tile>

                </v-list>
            </v-flex>

            <v-flex xs9 fill-height>
                <v-container fluid grid-list-md>

                    <div class="no-access" v-if="!this.$store.getters.loggedIn">

                        <h1>You don't have access to this information :(</h1>

                        <router-link :to="{ name: 'home' }">Get out of here</router-link>
                    </div>

                    <div v-if="this.$store.getters.loggedIn">
                        <transition name="fade">
                            <keep-alive>
                                <router-view></router-view>
                            </keep-alive>
                        </transition>
                    </div>
                </v-container>
            </v-flex>
        </v-layout>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';

@Component
export default class AdminHome extends Vue {

    private navDrawerOpen: boolean = true;
}
</script>

<style lang="less">
.admin-wrapper {
    position: relative;
    height: 100%;
    padding-left: 0 !important;
    padding-right: 0 !important;

    .slide-out-admin-options {
        background: white; // Match the list contained in it
    }
}
</style>
