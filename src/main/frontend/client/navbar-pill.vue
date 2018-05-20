<template>
    <v-btn flat class="navbar-pill" v-bind:class="{ active: isActiveTab() }" v-on:click="navigate">
        {{compound}}
        <i class="fa fa-times close-icon" aria-hidden="true" @click="close"></i>
    </v-btn>
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

    isActiveTab(): boolean {
        return RakUtil.isActiveTab(this.$route, this.compound);
    }

    navigate() {
        this.$router.push({ name: 'compound', params: { id: this.compound }});
    }

    close(e: MouseEvent) {
        this.$emit('close', this.compound);
        e.preventDefault();
        e.stopPropagation();
    }
}
</script>

<style lang="less">
@import "../styles/app-variables";
@close-icon-color: gray;

.navbar-pill-parent {
    position: relative;
    margin-right: 1rem;
}

/* pill styles are essentially copied from ui inverted menu */
.navbar-pill {

    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;

    &:hover, &.active {
        background: rgba(255, 255, 255, 0.15) !important;
        color: #fff;
    }

    &:hover .close-icon {
        opacity: 1;
        color: lighten(@close-icon-color, 25%);
    }

    .close-icon {

        transition: color @transition-time, opacity @transition-time;

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
