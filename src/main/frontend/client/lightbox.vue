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
                <img :src="image" width=400 height=400>

                <v-flex xs12 style="text-align: right">
                    <div style="display: inline-block">
                        <div class="lightbox-footer-content">
                            <div class="lightbox-save-label">
                                Save as:
                            </div>
                            <v-select
                                class="lightbox-save-select"
                                :items="downloadOptions"
                                value="png500500"
                                required
                                hide-details
                                ></v-select>
                            <v-btn flat color="primary" class="lightbox-save-button" @save="saveImage">
                                Save
                            </v-btn>
                        </div>
                    </div>
                </v-flex>
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

    private downloadOptions: any[] = [
        { text: 'PNG (200x200)', value: 'png200300' },
        { text: 'PNG (300x300)', value: 'png300300' },
        { text: 'PNG (500x500)', value: 'png500500' },
        { text: 'SVG', value: 'svg' }
    ];

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

    saveImage() {

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

        .lightbox-footer-content {

            display: flex;
            align-items: center;

            .lightbox-save-label {
                margin-right: 8px; // Matches button's margin-left
            }

            .lightbox-save-select {
                min-width: 12rem;
                padding-top: 0;
            }

            .lightbox-save-button {
                // Fit more snugly into bottom-right
                margin-top: 0;
                margin-right: 0;
                margin-bottom: 0;
            }
        }
    }
}
</style>
