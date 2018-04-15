<template>
    <v-container grid-list-md class="page-wrapper">
        <v-layout row wrap>

            <section-header>Results for {{this.id}}</section-header>

            <v-flex xs12>
                <result-table :filters="gridFilters"></result-table>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';
import SectionHeader from './header.vue';
import ResultTable from './result-table.vue';

@Component({ components: { ResultTable, SectionHeader } })
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
