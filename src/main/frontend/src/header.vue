<template>
    <v-flex xs12>
        <div class="headline pb-2 primary--text">
            <slot></slot>
        </div>
    </v-flex>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';

@Component
export default class SectionHeader extends Vue {
}
</script>

<style lang="less">
</style>
