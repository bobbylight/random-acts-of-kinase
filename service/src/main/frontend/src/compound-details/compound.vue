<template>
    <v-container grid-list-md class="page-wrapper">

        <v-row v-if="authorized">

            <section-header>
                Results for {{id}}
                <div style="float: right" v-if="$store.getters.loggedIn">
                    <v-tooltip bottom>
                        <template v-slot:activator="{ on }">
                            <v-btn text icon @click="edit"
                                    slot="activator"
                                    v-on="on">
                                <v-icon>fas fa-edit</v-icon>
                            </v-btn>
                        </template>
                        <span>Edit this compound</span>
                    </v-tooltip>
                </div>
            </section-header>

            <v-col cols="12">
                <compound-details-card ref="compoundDetailsCard" :compound="compound"></compound-details-card>
            </v-col>

            <v-col cols="12" class="card-vertical-spacing">
                <v-expansion-panels :value="0">
                    <v-expansion-panel class="expansion-panel-no-left-margin">
                        <v-expansion-panel-header>
                            <h3 class="headline">Table View</h3>
                        </v-expansion-panel-header>
                        <v-expansion-panel-content>
                            <result-table class="hidden-sm-and-down" :filters="gridFilters"></result-table>
                            <result-list class="hidden-md-and-up" :filters="gridFilters"></result-list>
                        </v-expansion-panel-content>
                    </v-expansion-panel>
                </v-expansion-panels>
            </v-col>

            <v-col cols="12" class="card-vertical-spacing" v-if="chartData">
                <v-expansion-panels>
                    <v-expansion-panel class="expansion-panel-no-left-margin">
                        <v-expansion-panel-header>
                            <h3 class="headline">Chart View</h3>
                        </v-expansion-panel-header>
                        <v-expansion-panel-content>
                            <column-chart :data="chartData"></column-chart>
                        </v-expansion-panel-content>
                    </v-expansion-panel>
                </v-expansion-panels>
            </v-col>
        </v-row>

        <v-row v-if="!authorized">
            <unauthorized/>
        </v-row>

        <EditCompoundModal :compound="compound" :show="showEditCompound"
                @updated="compoundUpdated" @close="showEditCompound = false">
        </EditCompoundModal>
    </v-container>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';
import SectionHeader from '@/header.vue';
import ResultTable from './result-table.vue';
import ResultList from './result-list.vue';
import CompoundDetailsCard from './compound-details-card.vue';
import EditCompoundModal from './edit-compound-modal.vue';
import restApi from '@/rest-api';
import { ActivityProfile, Compound, ErrorResponse, PagedDataRep } from '@/rak';
import unauthorized from '@/admin/unauthorized.vue';

@Component({ components: {
        CompoundDetailsCard,
        ResultTable,
        ResultList,
        SectionHeader,
        EditCompoundModal,
        unauthorized
} })
export default class CompoundView extends Vue {

    @Prop({ required: true })
    private readonly id: string;

    private compound: Compound | null = {
        compoundName: '',
        chemotype: '',
        s10: ''
    };

    private chartData: any[] | null = null;

    private showEditCompound: boolean = false;
    private authorized: boolean = true;

    compoundUpdated(newCompound: Compound) {
        //(this.$refs.compoundDetailsCard as CompoundDetailsCard).refresh();
        this.compound = newCompound;
    }

    edit() {
        this.showEditCompound = true;
    }

    get gridFilters(): any {
        return {
            inhibitor: this.id, //this.filters.inhibitor,
            kinase: '', //this.filters.kinase,
            activity: '' //this.filters.activity
        };
    }

    mounted() {

        const sort: any = null;

        restApi.getCompound(this.id)
            .then((compound: Compound) => {

                this.compound = compound;

                return restApi.getActivityProfiles(0, 10000, this.gridFilters, sort)
                    .then((allData: PagedDataRep<ActivityProfile>) => {
                        const activityProfiles: ActivityProfile[] = allData.data;
                        this.chartData = activityProfiles
                            .filter((profile: ActivityProfile) => {
                                return profile.percentControl <= 100;
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
            })
            .catch((errorResponse: ErrorResponse) => {
                this.authorized = false;
            });
    }
}
</script>

<style lang="less">
@import '../../styles/app-variables';

.expansion-panel-no-left-margin {
    margin-left: 0;
}

.card-vertical-spacing {
    margin-top: @card-vertical-spacing;
}
</style>
