<template>
    <v-container grid-list-md class="page-wrapper">

        <v-layout row wrap class="network-tab-wrapper">

            <section-header>Network</section-header>

            <v-flex xs12 class="card-vertical-spacing">
                <v-expansion-panel class="expansion-panel-no-left-margin" :value="0">
                    <v-expansion-panel-content>
                        <div slot="header">
                            <h3 class="headline">Network Properties</h3>
                        </div>
                        <v-card>
                            <v-card-text>

                                <v-autocomplete :loading="loading"
                                                :items="compounds"
                                                :search-input.sync="search"
                                                v-model="selectedCompounds"
                                                @change="selectedCompoundsChanged"
                                                cache-items
                                                hide-no-data
                                                hide-details
                                                label="Compounds"
                                                chips
                                                item-text="compoundName"
                                                :return-object="true"
                                                multiple>
                                    <template slot="selection" slot-scope="data">
                                        <v-chip :selected="data.selected"
                                                close
                                                class="chip--select-multi"
                                                @input="removeCompound(data.item)">
                                            {{ data.item.compoundName }}
                                        </v-chip>
                                    </template>
                                </v-autocomplete>

                                <v-layout row>
                                    <v-flex xs6>
                                        <v-text-field type="number" label="% Control" class="search-field right-aligned"
                                                      :rules="numericValidationRules(100)" v-model="percentControl"
                                                      :step="1" :min="0" :max="100" suffix="%">
                                        </v-text-field>
                                    </v-flex>
                                    <v-flex xs6>
                                        <v-text-field :disabled="true"
                                                      type="number" label="Kd" class="search-field right-aligned"
                                                      :rules="numericValidationRules(10000)" v-model="kd"
                                                      :step="1" :min="0" :max="10000" suffix="nM">
                                        </v-text-field>
                                    </v-flex>
                                </v-layout>

                                <v-card-actions>
                                    <v-spacer></v-spacer>
                                    <v-btn :disabled="formInvalid" color="primary" @click="rerender">
                                        Rerender
                                    </v-btn>
                                </v-card-actions>
                            </v-card-text>
                        </v-card>
                    </v-expansion-panel-content>
                </v-expansion-panel>
            </v-flex>

            <v-flex xs12 class="card-vertical-spacing">
                <v-expansion-panel class="expansion-panel-no-left-margin" :value="0">
                    <v-expansion-panel-content>
                        <div slot="header">
                            <h3 class="headline">Network View</h3>
                        </div>
                        <v-card>
                            <v-card-text>
                                <compound-network :compounds="networkCompounds"
                                                  :filters="filters"></compound-network>
                            </v-card-text>
                        </v-card>
                    </v-expansion-panel-content>
                </v-expansion-panel>
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

@Component({ components: { SectionHeader, CompoundNetwork } })
export default class CompoundNetworkTab extends Vue {

    private loading: boolean = false;
    private percentControl: string = '20';
    private kd: string = '';
    private filters: any = {};
    private compounds: Compound[] = [];
    private networkCompounds: Compound[] = [];
    private search: string = '';
    private selectedCompounds: Compound[] = [];
    private formInvalid: boolean = true;

    private numericValidationRules(max: number): any[] {

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

    mounted() {
        // Set initial values
        this.filters = {
            activity: this.percentControl,
            kd: this.kd
        };
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
        this.formInvalid = !this.selectedCompounds.length;
    }

    rerender() {
        this.networkCompounds = this.selectedCompounds.slice();
        this.filters = {
            activity: this.percentControl,
            kd: this.kd
        };
    }

    /**
     * Fires when new values are added.  Values being removed by x-ing out a Chip do not
     * call this method; see removeCompound() for that code path.
     *
     * @param newValue The new array of selected compounds.
     */
    selectedCompoundsChanged(newValue: Compound[]) {
        this.formInvalid = !newValue.length;
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
