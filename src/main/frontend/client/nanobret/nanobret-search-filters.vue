<template>
    <v-layout row wrap>

        <v-flex xs12>
            <v-text-field type="text" label="Search by compound" class="search-field"
                          v-model="nanoBretFilters.inhibitor"></v-text-field>
        </v-flex>

        <v-flex xs12>
            <v-layout row wrap>

                <v-flex xm8>
                    <lazy-dropdown v-model="nanoBretFilters.kinase"
                                   id="kinase"
                                   label="Or by kinase and IC50"
                                   name="kinase" url="api/kinases"
                                   :queryParams="kinaseQueryParams"
                                   filterParamName="discoverx"
                                   responseLabelField="discoverxGeneSymbol"
                                   responseValueField="responseValueField"
                                   :transform="kinaseResponseTransformer"></lazy-dropdown>
                </v-flex>

                <v-flex sm4>
                    <v-text-field type="number" label="IC50" class="search-field right-aligned"
                                  :rules="ic50ValidationRules" v-model="nanoBretFilters.ic50"
                                  :step="0.1" min="0" max="10000" suffix="nM">
                    </v-text-field>
                </v-flex>
            </v-layout>
        </v-flex>
    </v-layout>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import LazyDropdown from '../lazy-dropdown.vue';
import { Kinase, NanoBretSearchFilter, PagedDataRep } from '../rak';
import ActivityOrKdField from '../search/activity-or-kd-field.vue';

@Component({ components: { LazyDropdown } })
export default class NanoBretSearchFilters extends Vue {

    private ic50ValidationRules: any[] = [

        (value: number) => {
            const max: number = 10000;
            return +value >= 0 && +value <= max || `Must be between 0 and ${max}`;
        }
    ];

    get nanoBretFilters(): NanoBretSearchFilter {
        return this.$store.state.nanoBretFilters;
    }

    private kinaseQueryParams: any = { size: 1000 };

    private kinaseResponseTransformer(response: PagedDataRep<Kinase>): any[] {
        const choices: any[] = response.data.map((kinase: Kinase) => {
            const discoverx: string = kinase.discoverxGeneSymbol;
            return { discoverxGeneSymbol: discoverx, responseValueField: discoverx };
        });
        //choices.unshift({ discoverxGeneSymbol: '', responseValueField: '' });
        return choices;
    }
}
</script>

<style lang="less">
</style>
