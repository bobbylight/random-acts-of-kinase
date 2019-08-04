<template>
    <v-container grid-list-md class="page-wrapper">

        <v-layout row wrap v-if="authorized">

            <section-header>
                Results for {{id}}
                <div style="float: right" v-if="$store.getters.loggedIn">
                    <v-tooltip bottom>
                        <v-btn flat icon @click="edit"
                                slot="activator">
                            <v-icon>fas fa-edit</v-icon>
                        </v-btn>
                        <span>Edit this compound</span>
                    </v-tooltip>
                </div>
            </section-header>

            <v-flex xs12>
                <compound-details-card ref="compoundDetailsCard" :compound="compound"></compound-details-card>
            </v-flex>


            <v-flex xs12 class="card-vertical-spacing">
                <v-expansion-panel class="expansion-panel-no-left-margin" :value="0">
                    <v-expansion-panel-content>
                        <div slot="header">
                            <h3 class="headline">Table View</h3>
                        </div>
                        <v-card>
                            <v-card-text>
                                <result-table :filters="gridFilters"></result-table>
                            </v-card-text>
                        </v-card>
                    </v-expansion-panel-content>
                </v-expansion-panel>
            </v-flex>

            <v-flex xs12 class="card-vertical-spacing" v-if="chartData">
                <v-expansion-panel class="expansion-panel-no-left-margin">
                    <v-expansion-panel-content>
                        <div slot="header">
                            <h3 class="headline">Chart View</h3>
                        </div>
                        <v-card>
                            <v-card-text>
                                <column-chart :data="chartData"></column-chart>
                            </v-card-text>
                        </v-card>
                    </v-expansion-panel-content>
                </v-expansion-panel>
            </v-flex>
        </v-layout>

        <v-layout row wrap v-if="!authorized">
            <unauthorized/>
        </v-layout>

        <EditCompoundModal :compound="compound" :show="showEditCompound"
                @updated="compoundUpdated" @close="showEditCompound = false">
        </EditCompoundModal>
    </v-container>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';
import SectionHeader from './header.vue';
import ResultTable from './result-table.vue';
import CompoundDetailsCard from './compound-details-card.vue';
import EditCompoundModal from './edit-compound-modal.vue';
import restApi from './rest-api';
import { ActivityProfile, Compound, ErrorResponse, PagedDataRep } from './rak';
import unauthorized from './admin/unauthorized.vue';

@Component({ components: { CompoundDetailsCard, ResultTable, SectionHeader, EditCompoundModal, unauthorized } })
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
@import '../styles/app-variables';

.expansion-panel-no-left-margin {
    margin-left: 0;
}

.card-vertical-spacing {
    margin-top: @card-vertical-spacing;
}
</style>
