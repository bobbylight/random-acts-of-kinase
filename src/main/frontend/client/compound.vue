<template>
    <div class="ui container compound-results">
        <div class="ui header">Results for {{this.id}}</div>
        <result-table :filters="gridFilters"></result-table>
    </div>
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
.compound-results {
    padding-top: 1rem; // Matches search.vue
}
</style>
