<template>
    <v-container grid-list-md class="page-wrapper">

        <v-layout row wrap class="network-tab-wrapper">

            <section-header>Network</section-header>

            <v-flex xs12>
                <v-card class="expansion-panel-no-left-margin">
                    <actionable-card-title title="Compound Network"
                            sub-title="View compounds and activity profiles in a network diagram">
                        <v-tooltip right>
                            <template v-slot:activator="{ on }">
                                <v-menu :close-on-content-click="false">
                                    <template v-slot:activator="{ on: on2 }">
                                        <v-btn fab text v-on="{ ...on, ...on2 }">
                                            <v-icon>settings</v-icon>
                                        </v-btn>
                                    </template>

                                    <v-card class="network-properties-popup">
                                        <v-card-title class="network-properties-popup-title">
                                            <div class="headline">Network Properties</div>
                                        </v-card-title>
                                        <v-card-text class="network-properties-popup-content">
                                            <v-container grid-list-md class="network-properties-popup-container">
                                                <v-layout row wrap>
                                                    <v-flex xs12>
                                                        <v-switch
                                                            :label="`${physicsEnabled ? 'Physics' : 'No physics'}`"
                                                            v-model="physicsEnabled"></v-switch>
                                                    </v-flex>
                                                    <v-flex xs12>
                                                        Other network properties will go here.
                                                    </v-flex>
                                                </v-layout>
                                            </v-container>
                                        </v-card-text>
                                    </v-card>
                                </v-menu>
                            </template>
                            <span>Network Properties</span>
                        </v-tooltip>
                    </actionable-card-title>
                    <v-card-text>
                        <v-layout row wrap>

                            <v-flex xs12 px-4>
                                <v-form ref="form" lazy-validation>
                                    <v-autocomplete :loading="loading"
                                                    :items="compounds"
                                                    :search-input.sync="search"
                                                    v-model="selectedCompounds"
                                                    @change="selectedCompoundsChanged"
                                                    cache-items
                                                    hide-no-data
                                                    label="Compounds"
                                                    chips
                                                    item-text="compoundName"
                                                    :return-object="true"
                                                    autocomplete="off"
                                                    multiple
                                                    :rules="compoundDropdownRules()">
                                        <template slot="selection" slot-scope="data">
                                            <v-chip :input-value="data.selected"
                                                    close
                                                    class="chip--select-multi"
                                                    @input="removeCompound(data.item)">
                                                {{ data.item.compoundName }}
                                            </v-chip>
                                        </template>
                                    </v-autocomplete>
                                </v-form>
                            </v-flex>
                        </v-layout>

                        <v-layout row wrap>
                            <v-flex xs6 px-4>
                                <v-slider :min="1" v-model="percentControl"
                                          class="network-slider"
                                          hide-details
                                          :validate-on-blur="true"
                                          :thumb-label="true" label="% Control"></v-slider>
                            </v-flex>
                            <v-flex xs6 px-4>
                                <v-text-field :disabled="true"
                                              type="number" label="Kd" class="search-field right-aligned"
                                              :rules="numericValidationRules(10000)" v-model="kd"
                                              :step="1" :min="0" :max="10000" suffix="nM">
                                </v-text-field>
                            </v-flex>


                            <v-flex xs12 px-4>
                                <v-card-actions>
                                    <v-spacer></v-spacer>
                                    <v-btn :disabled="formInvalid" color="primary" @click="rerender">
                                        Rerender
                                    </v-btn>
                                </v-card-actions>
                            </v-flex>

                            <v-flex xs12>
                                <compound-network :compounds="networkCompounds"
                                                  :physics-enabled="physicsEnabled"
                                                  :percent-control="percentControl"></compound-network>
                            </v-flex>
                        </v-layout>
                    </v-card-text>
                </v-card>
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Watch } from 'vue-property-decorator';
import SectionHeader from './header.vue';
import CompoundNetwork from './compound-network.vue';
import restApi from './rest-api';
import { Compound, PagedDataRep, SearchFilter } from './rak';
import ActionableCardTitle from './actionable-card-title.vue';

const MAX_COMPOUND_COUNT: number = 6;

@Component({ components: { ActionableCardTitle, SectionHeader, CompoundNetwork } })
export default class CompoundNetworkTab extends Vue {

    private loading: boolean = false;
    percentControl: string = '20';
    kd: string = '';
    private compounds: Compound[] = [];
    private networkCompounds: Compound[] = [];
    search: string = '';
    private selectedCompounds: Compound[] = [];
    private formInvalid: boolean = true;
    physicsEnabled: boolean = true;

    private compoundDropdownRules(): Function[] {

        return [

            (value: Compound[]) => {
                return value.length <= MAX_COMPOUND_COUNT || `Can't render more than ${MAX_COMPOUND_COUNT} compounds`;
            }
        ];
    }

    private numericValidationRules(max: number): Function[] {

        return [

            (value: string) => {
                return CompoundNetworkTab.isEmpty(value) || !isNaN(parseFloat(value)) || 'Must be a number';
            },

            (value: string) => {
                if (!parseFloat(value)) {
                    return true; // Will be caught by the rule above
                }
                return +value >= 0 && +value <= max || `Must be between 0 and ${max}`;
            }
        ];
    }

    private static isEmpty(text: string): boolean {
        // text might be a number
        return !text || !text.trim || !text.trim().length;
    }

    @Watch('search')
    private onSearchChanged(newVal: string) {
        if (newVal) {
            this.updateDropdown();
        }
    }

    removeCompound(compound: Compound) {
        this.selectedCompounds = this.selectedCompounds.filter((c2: Compound) => {
            return c2.compoundName !== compound.compoundName;
        });
        // form.validate() needs one update cycle to see new value of this.selectedCompounds
        setInterval(() => {
            this.updateSubmitButtonState();
        }, 0);
    }

    rerender() {
        this.networkCompounds = this.selectedCompounds.slice();
    }

    /**
     * Fires when new values are added.  Values being removed by x-ing out a Chip do not
     * call this method; see removeCompound() for that code path.
     *
     * @param newValue The new array of selected compounds.
     */
    selectedCompoundsChanged(newValue: Compound[]) {
        this.updateSubmitButtonState();
    }

    private updateSubmitButtonState() {
        console.log('Updating submit button...');
        this.formInvalid = !(this.$refs.form as any).validate();
    }

    private updateDropdown() {

        this.loading = true;

        const filter: SearchFilter = {
            inhibitor: this.search,
            kinase: '',
            activity: '',
            kd: '',
            activityOrKd: 'kd'
        };

        restApi.getCompounds(0, 10, filter)
            .then((pagedData: PagedDataRep<Compound>) => {
                this.compounds = pagedData.data;
                this.loading = false;
                return pagedData;
            });
    }
}
</script>

<style lang="less">
.network-slider {
    margin-top: 16px;
}

.network-properties-popup {

    z-index: 100;

    .network-properties-popup-title {
        padding-bottom: 0;
    }

    .network-properties-popup-content {

        padding: 0;

        .network-properties-popup-container {
            padding-top: 0;
        }
    }
}
</style>
