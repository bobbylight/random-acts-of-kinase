<template>
    <v-container grid-list-md class="page-wrapper">
        <v-layout row wrap>

            <section-header>Results for {{this.id}}</section-header>

            <v-flex xs12>
                <compound-details-card :compound-name="id"></compound-details-card>
            </v-flex>

            <v-flex xs12 v-if="chartData">
                <column-chart :data="chartData"></column-chart>
            </v-flex>

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
import CompoundDetailsCard from './compound-details-card.vue';
import restApi from './rest-api';
import { ActivityProfile, PagedDataRep } from './rak';

@Component({ components: { CompoundDetailsCard, ResultTable, SectionHeader } })
export default class Compound extends Vue {

    @Prop({ required: true })
    private id: string;

    private chartData: any[] | null = null;

    get gridFilters(): any {
        return {
            inhibitor: this.id, //this.filters.inhibitor,
            kinase: '', //this.filters.kinase,
            activity: '' //this.filters.activity
        };
    }

    mounted() {
        //this.chartData = [['Work', 32], ['Play', 1492]];

        const sort: any = null;

        return restApi.getActivityProfiles(0, 10000, this.gridFilters, sort)
            .then((allData: PagedDataRep<ActivityProfile>) => {
                const activityProfiles: ActivityProfile[] = allData.data;
                this.chartData = activityProfiles
                    .filter((profile: ActivityProfile) => {
                        return profile.percentControl < 100;
                    })
                    .sort((a: ActivityProfile, b: ActivityProfile) => {
                        if (b.percentControl < a.percentControl) {
                            return -1;
                        }
                        return b.percentControl > a.percentControl ? 1 : 0;
                    })
                    .map((profile: ActivityProfile) => {
                        return [
                            profile.kinase.discoverxGeneSymbol,
                            profile.percentControl
                        ];
                    });
            });
    }
}
</script>

<style lang="less">
</style>
