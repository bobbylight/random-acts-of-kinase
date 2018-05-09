<template>
    <transition name="fade">
        <div class="lightbox"
             :class="{ 'visible-lightbox': show }"
             @click="hide"
             v-if="show">

            <img :src="image" width=600 height=600>
        </div>
    </transition>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop, Watch } from 'vue-property-decorator';

@Component
export default class Lightbox extends Vue {

    @Prop({ required: true })
    private image: string | undefined;

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
}
</style>
