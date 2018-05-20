<template>
    <transition name="fade">
        <div class="lightbox"
             :class="{ 'visible-lightbox': show }"
             @click="hide"
             v-if="show">

            <div class="lightbox-content-wrapper elevation-1 pa-3"
                    @click.stop="">
                <div class="lightbox-close-icon" @click="hide">
                    <v-tooltip right>
                        <v-icon slot="activator">close</v-icon>
                        <span>Close</span>
                    </v-tooltip>
                </div>
                <section-header>{{title}}</section-header>
                <img :src="image" width=600 height=600>
            </div>
        </div>
    </transition>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop, Watch } from 'vue-property-decorator';
import SectionHeader from './header.vue';

@Component({ components: { SectionHeader } })
export default class Lightbox extends Vue {

    @Prop()
    private title: string | undefined;

    @Prop({ required: true })
    private image: string;

    private show: boolean = false;

    @Watch('image')
    private onImageChanged(newValue: string | undefined) {
        this.show = !!newValue;
        if (this.show) {
            document.addEventListener('keydown', this.keyHandler);
        }
    }

    private hide() {
        this.$emit('hide');
    }

    keyHandler(e: KeyboardEvent) {
        if (e.key === 'Escape') {
            document.removeEventListener('keydown', this.keyHandler);
            this.hide();
        }
    }
}
</script>

<style lang="less">
@import '../styles/app-variables';

.lightbox {

    color: black;
    background: #ffffff40;
    z-index: 10000;

    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;

    display: flex;
    justify-content: center;
    align-items: center;

    .lightbox-content-wrapper {

        position: relative;
        background: white;

        .lightbox-close-icon {

            position: absolute;
            top: 1rem;
            right: 1rem;
            cursor: pointer;

            color: gray;
            transition: color @transition-time;

            &:hover i {
                color: black;
            }
        }
    }
}
</style>
