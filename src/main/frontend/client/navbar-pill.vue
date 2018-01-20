<template>
    <div class="navbar-pill-parent">
        <div class="ui button pill" v-bind:class="{ active: isActiveTab() }"
                v-on:click="navigate">{{compound}}
            <i class="fa fa-times close-icon" aria-hidden="true" @click="close"></i>
        </div>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';

@Component
export default class NavbarPill extends Vue {

    @Prop({ required: true })
    compound: string;

    isActiveTab(): boolean {
        const pathRegex: RegExp = new RegExp(this.compound + '$');
        return !!this.$route.fullPath.match(pathRegex);
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

    @close-icon-color: gray;
    @transition-time: .5s;

    .navbar-pill-parent {
        position: relative;
        margin-right: 1rem;
    }

    /* pill styles are essentially copied from ui inverted menu */
    .ui.button.pill {
        background: rgba(255, 255, 255, 0.08);
        color: rgba(255,255,255,.9);
        font-weight: 400;
        padding-top: 0.5rem;
        padding-bottom: 0.5rem;
        border-radius: 0.8rem;
        transition: color @transition-time ease,
                    background-color @transition-time ease;

        &:hover, &.active {
            background: rgba(255, 255, 255, 0.15);
            color: #fff;

            .close-icon {
                color: lighten(@close-icon-color, 25%);
            }
        }

        .close-icon {
            color: @close-icon-color;
            font-size: 1.3rem;
            position: absolute;
            top: -6px;
            right: 0;
            transition: color @transition-time ease;
            &:hover {
                color: white;
            }
        }
    }
</style>
