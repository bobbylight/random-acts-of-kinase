<template>
    <v-container grid-list-md>
        <v-layout row wrap class="compound-details-wrapper py-5">
            <div class="headline pb-2 primary--text">Results for {{this.id}}</div>
            <result-table :filters="gridFilters"></result-table>
        </v-layout>
    </v-container>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';
import ResultTable from './result-table.vue';

@Component({ components: { ResultTable } })
export default class Compound extends Vue {

    @Prop({ required: true })
    private id: string;

    get gridFilters() {
        return {
            inhibitor: this.id, //this.filters.inhibitor,
            kinase: '', //this.filters.kinase,
            activity: '' //this.filters.activity
        };
    }
}
</script>

<style lang="less">
</style>
