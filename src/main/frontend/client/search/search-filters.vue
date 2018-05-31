<template>
    <v-layout row wrap>

        <v-flex xs12>
            <v-text-field type="text" label="Search by compound" class="search-field"
                          v-model="filters.inhibitor"></v-text-field>
        </v-flex>

        <v-flex xs12>
            <v-layout row wrap>

                <v-flex xm8>
                    <lazy-dropdown v-model="filters.kinase"
                                   id="kinase"
                                   label="Or by kinase and activity"
                                   name="kinase" url="api/kinases"
                                   :queryParams="kinaseQueryParams"
                                   filterParamName="discoverx"
                                   responseLabelField="discoverxGeneSymbol"
                                   responseValueField="responseValueField"
                                   :transform="kinaseResponseTransformer"></lazy-dropdown>
                </v-flex>

                <v-flex sm4>
                    <activity-or-kd-field v-model="filters.activity"></activity-or-kd-field>
                </v-flex>
            </v-layout>
        </v-flex>
    </v-layout>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';
import LazyDropdown from '../lazy-dropdown.vue';
import { Kinase, PagedDataRep } from '../rak';
import ActivityOrKdField from './activity-or-kd-field';

@Component({ components: { ActivityOrKdField, LazyDropdown } })
export default class SearchFilters extends Vue {

    @Prop({ required: true })
    filters: any;

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
.search-field {
    width: 100%;

    &.right-aligned {
        input {
            text-align: right;
        }
    }
}
.ui.input.search-field.error {
    .ui.label {
        border: 1px solid #e0b4b4; // Matches input's error border
    }
}
</style>
