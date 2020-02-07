<template>
    <v-col cols="12">
        <div class="rak-header headline pb-2 primary--text">
            <slot></slot>
        </div>
    </v-col>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';

@Component
export default class SectionHeader extends Vue {
}
</script>

<style lang="less">
.v-application .rak-header.headline {
    padding-top: .5rem !important;
    padding-bottom: 0 !important;
    font-size: 2rem !important;
}
</style>
