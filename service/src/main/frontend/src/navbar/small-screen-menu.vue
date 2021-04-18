<template>
    <v-list dark class="small-screen-menu">

        <v-list-item-group class="action-group">
            <v-list-item @click="setActiveTab('search')">
                <v-list-item-icon>
                    <v-icon>fa-search</v-icon>
                </v-list-item-icon>
                <v-list-item-title>Search</v-list-item-title>
            </v-list-item>
            <v-list-item @click="setActiveTab('blog')">
                <v-list-item-icon>
                    <v-icon>fa-newspaper</v-icon>
                </v-list-item-icon>
                <v-list-item-title>News</v-list-item-title>
            </v-list-item>
            <v-list-item @click="setActiveTab('network')">
                <v-list-item-icon>
                    <v-icon>mdi-chart-bubble</v-icon>
                </v-list-item-icon>
                <v-list-item-title>Network</v-list-item-title>
            </v-list-item>
            <v-list-item @click="setActiveTab('partners')">
                <v-list-item-icon>
                    <v-icon>fa-handshake</v-icon>
                </v-list-item-icon>
                <v-list-item-title>Partners</v-list-item-title>
            </v-list-item>
            <v-list-item @click="showFeedback()">
                <v-list-item-icon>
                    <v-icon>fa-comments</v-icon>
                </v-list-item-icon>
                <v-list-item-title>Feedback</v-list-item-title>
            </v-list-item>
            <v-list-item v-if="$store.getters.loggedIn" @click="setActiveTab($store.state.lastAdminRouteName)">
                <v-list-item-icon>
                    <v-icon>fa-user</v-icon>
                </v-list-item-icon>
                <v-list-item-title>
                    {{ $store.getters.loggedIn ? 'Admin' : ' ' }}
                </v-list-item-title>
            </v-list-item>
            <v-list-item v-if="!$store.getters.loggedIn">
                <v-list-item-icon>
                    &nbsp;
                </v-list-item-icon>
                <v-list-item-title>
                    &nbsp;
                </v-list-item-title>
            </v-list-item>
        </v-list-item-group>

        <v-subheader v-if="openCompounds.length">Open Compounds</v-subheader>
        <v-list-item-group>
            <v-list-item v-for="compound in openCompounds" :key="compound" @click="showCompoundDetails(compound)">
                <v-list-item-icon>
                    <v-img src="/img/molecule-white.svg"
                           style="width: 1rem">
                    </v-img>
                </v-list-item-icon>
                <v-list-item-title>{{ compound }}</v-list-item-title>
                <v-list-item-action>
                    <v-icon class="red--text text--accent-1" @click="closeCompound(compound)">
                        mdi-close-circle
                    </v-icon>
                </v-list-item-action>
            </v-list-item>
        </v-list-item-group>

    </v-list>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';

@Component
export default class SmallScreenMenu extends Vue {

    @Prop({ required: true })
    openCompounds!: string[];

    private close() {
        this.$emit('close');
    }

    private closeCompound(compound: string) {
        this.close();
        this.$emit('closeCompound', compound);
    }

    private setActiveTab(tabName: string) {
        this.close();
        this.$router.push({ name: tabName });
    }

    private showCompoundDetails(compound: string) {
        this.close();
        this.$emit('showCompoundDetails', compound);
    }

    private showFeedback() {
        this.close();
        this.$emit('feedback');
    }
}
</script>

<style lang="less">
@app-bar-offset: 56px; /* Matches banner size */

.small-screen-menu {

    padding-top: @app-bar-offset !important;
    background-color: #272727 !important;
    margin-bottom: -@app-bar-offset;

    .action-group {
        column-count: 2;
    }
}
</style>
