<template>
    <v-select
        class="import-filter-select"
        :items="[ 'none', 'new', 'modified', 'unmodified' ]"
        :disabled="disabled"
        @change="fireChangeEvent($event)"
        label="Filter"
        single-line
        auto
        prepend-icon="fa-filter"
        hide-details
    ></v-select>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';

@Component
export default class ImportPreviewTableFilter extends Vue {

    @Prop({ required: true })
    private disabled: boolean;

    fireChangeEvent($event: any) {
        this.$emit('change', $event);
    }
}
</script>

<style lang="less">

.import-filter-select {
    padding-top: 0 !important;

    label {
        top: 0;
    }
}
</style>
