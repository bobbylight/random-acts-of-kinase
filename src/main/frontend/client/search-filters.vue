<template>
    <v-layout row wrap>

        <v-flex xs12>
            <div>Search by compound:</div>
            <div class="ui grid">
                <div class="sixteen wide column">
                    <v-text-field type="text" placeholder="Inhibitor" class="search-field"
                                  v-model="filters.inhibitor"></v-text-field>
                </div>
            </div>
        </v-flex>

        <v-flex xs12>

            <div style="margin-top: 1rem">Or by kinase and activity:</div>

            <v-layout row wrap>

                <v-flex xm8>
                    <!--<search-field type="kinase" v-model="filters.kinase"-->
                    <!--place-holder="Kinase"></search-field>-->
                    <lazy-dropdown v-model="filters.kinase"
                                   id="kinase"
                                   name="kinase" url="api/kinases"
                                   :queryParams="kinaseQueryParams"
                                   filterParamName="discoverx"
                                   responseLabelField="discoverxGeneSymbol"
                                   reponseValueField="discoverxGeneSymbol"
                                   :transform="kinaseResponseTransformer"></lazy-dropdown>
                </v-flex>

                <v-flex sm4>
                    <v-text-field type="number" placeholder="Remaining activity" class="search-field right-aligned"
                                  :rules="numericValidationRules" v-model="filters.activity"
                                  step="0.1" min="0.1" max="100"></v-text-field>
                </v-flex>
            </v-layout>
        </v-flex>
    </v-layout>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';
import SearchField from './search-field.vue';
import LazyDropdown from './lazy-dropdown.vue';
import { Kinase, PagedDataRep } from './rak';

@Component({ components: { SearchField, LazyDropdown } })
export default class SearchFilters extends Vue {

    @Prop({ required: true })
    filters: any;

    private kinaseQueryParams: any = { size: 1000 };

    private static isEmpty(text: string): boolean {
        return !text.trim().length;
    }

    get numericValidationRules(): any[] {

        const rules: any = [];

        rules.push((value: string) => {
            return SearchFilters.isEmpty(value) || !isNaN(parseFloat(value)) || 'Must be a number';
        });
        rules.push((value: string) => {
            if (!parseFloat(value)) {
                return true; // Will be caught by the rule above
            }
            return +value >= 0 && +value <= 100 || 'Must be between 0 and 100';
        });

        return rules;
    }

    private kinaseResponseTransformer(response: PagedDataRep<Kinase>): string[] {
        return response.data.map((kinase: Kinase) => {
            return kinase.discoverxGeneSymbol;
        });
    }
}
</script>

<style lang="less">
    .search-field {
        width: 100%;

        &.right-aligned {
            text-align: right;
        }
    }
    .ui.input.search-field.error {
        .ui.label {
            border: 1px solid #e0b4b4; // Matches input's error border
        }
    }
</style>
