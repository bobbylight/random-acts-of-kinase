<template>
    <div class="navbar-pill-wrapper">
        <v-btn flat class="navbar-pill" v-bind:class="{ active: isActiveTab() }" @click="navigate">
            {{compound}}
        </v-btn>
        <i class="fa fa-times close-icon" aria-hidden="true" @click="close"></i>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';
import RakUtil from './util';

@Component
export default class NavbarPill extends Vue {

    @Prop({ required: true })
    compound: string;

    close(e: MouseEvent) {
        this.$emit('close', this.compound);
    }

    isActiveTab(): boolean {
        return RakUtil.isActiveTab(this.$route, this.compound);
    }

    navigate() {
        this.$router.push({ name: 'compound', params: { id: this.compound }});
    }
}
</script>

<style lang="less">
@import '../styles/app-variables';
@close-icon-color: gray;

.navbar-pill-wrapper {

    height: inherit;
    position: relative;

    /* pill styles are essentially copied from ui inverted menu */
    .navbar-pill {

        text-overflow: ellipsis;
        overflow: hidden;
        white-space: nowrap;

        &:hover, &.active {
            background: rgba(255, 255, 255, 0.15) !important;
            color: #fff;
        }
    }

    &:hover .close-icon {
        opacity: 1;
        color: lighten(@close-icon-color, 25%);
    }

    .close-icon {

        transition: color @transition-time, opacity @transition-time;

        cursor: pointer;
        opacity: 0;
        color: @close-icon-color;
        font-size: 1.3rem;
        position: absolute;
        top: 3px;
        right: 3px;

        &:hover {
            color: white;
        }
    }
}
</style>
