<template>
    <v-row>

        <v-col cols="12">
            <v-text-field type="text" label="Search by compound" class="search-field"
                          autocomplete="off" hide-details
                          v-model="nanoBretFilters.inhibitor"></v-text-field>
        </v-col>

        <v-col md="8">
            <lazy-dropdown v-model="nanoBretFilters.kinase"
                           id="kinase"
                           label="Or by kinase and IC50"
                           name="kinase" url="api/kinases"
                           :queryParams="kinaseQueryParams"
                           filterParamName="entrez"
                           responseLabelField="discoverxGeneSymbol"
                           responseValueField="responseValueField"
                           :transform="kinaseResponseTransformer"></lazy-dropdown>
        </v-col>

        <v-col sm="4">
            <v-text-field type="number" label="IC50" class="search-field right-aligned"
                          :rules="ic50ValidationRules" v-model="nanoBretFilters.ic50"
                          autocomplete="off" hide-details
                          :step="0.1" min="0" max="10000" suffix="nM">
            </v-text-field>
        </v-col>
    </v-row>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import LazyDropdown from '../lazy-dropdown.vue';
import { Kinase, NanoBretSearchFilter, PagedDataRep } from '../rak';

@Component({ components: { LazyDropdown } })
export default class NanoBretSearchFilters extends Vue {

    private readonly ic50ValidationRules: any[] = [

        (value: number) => {
            const max: number = 10000;
            return +value >= 0 && +value <= max || `Must be between 0 and ${max}`;
        }
    ];

    get nanoBretFilters(): NanoBretSearchFilter {
        return this.$store.state.nanoBretFilters;
    }

    private readonly kinaseQueryParams: any = { size: 1000 };

    private kinaseResponseTransformer(response: PagedDataRep<Kinase>): any[] {
        const choices: any[] = response.data.map((kinase: Kinase) => {
            const entrez: string = kinase.discoverxGeneSymbol;
            return { discoverxGeneSymbol: entrez, responseValueField: entrez };
        });
        //choices.unshift({ discoverxGeneSymbol: '', responseValueField: '' });
        return choices;
    }
}
</script>

<style lang="less">
</style>
