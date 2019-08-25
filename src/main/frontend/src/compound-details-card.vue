<template>
    <v-card class="compound-details-card">
        <v-card-title class="compound-details-card-title">
            <h3 class="headline">General Information</h3>
        </v-card-title>

        <v-card-text class="pt-0 pb-0">
            <v-container grid-list-md>
                <v-layout row wrap align-center>

                    <v-flex xs3 pt-0>
                        <v-tooltip bottom>
                            <template v-slot:activator="{ on }">
                                <img v-if="compoundImageUrl"
                                     :src="compoundImageUrl"
                                     class="compound-image"
                                     @click="onImageClicked"
                                     width="200" height="200"
                                     v-on="on">
                            </template>
                            <span>Click to enlarge</span>
                        </v-tooltip>
                    </v-flex>

                    <v-flex xs9 pt-0 pl-5>

                        <div class="compound-details-table">

                            <div class="compound-details-table-row">
                                <div class="compound-details-table-cell compound-details-table-cell-header">Compound:</div>
                                <div class="compound-details-table-cell">{{compound.compoundName}}</div>
                            </div>

                            <div class="compound-details-table-row">
                                <div class="compound-details-table-cell compound-details-table-cell-header">Chemotype:</div>
                                <div class="compound-details-table-cell">{{compound.chemotype}}</div>
                            </div>

                            <div class="compound-details-table-row">
                                <div class="compound-details-table-cell compound-details-table-cell-header">s(10):</div>
                                <div class="compound-details-table-cell">{{compound.s10}}</div>
                            </div>

                            <div class="compound-details-table-row">
                                <div class="compound-details-table-cell compound-details-table-cell-header">Solubility (&#181;g/mL):</div>
                                <div class="compound-details-table-cell">{{compound.solubility}}</div>
                            </div>

                            <div class="compound-details-table-row">
                                <div class="compound-details-table-cell compound-details-table-cell-header">SMILES:</div>
                                <div class="compound-details-table-cell">{{compound.smiles}}</div>
                            </div>

                            <div class="compound-details-table-row">
                                <div class="compound-details-table-cell compound-details-table-cell-header">Source:</div>
                                <div class="compound-details-table-cell">{{compound.source}}</div>
                            </div>

                            <div class="compound-details-table-row">
                                <div class="compound-details-table-cell compound-details-table-cell-header">Reference:</div>
                                <div class="compound-details-table-cell" v-html="reference"></div>
                            </div>
                        </div>
                    </v-flex>
                </v-layout>
            </v-container>
        </v-card-text>
    </v-card>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';
import restApi from './rest-api';
import { Compound, ErrorResponse } from './rak';
import Toaster from './toaster';

@Component
export default class CompoundDetailsCard extends Vue {

    @Prop({ required: true })
    private readonly compound: Compound;

    private compoundName: string | null | undefined = null;

    get compoundImageUrl(): string | null {
        if (this.compound && this.compound.compoundName) {
            return `api/compounds/images/${this.compound.compoundName}`;
        }
        return null;
    }

    get reference() {

        if (this.compound.primaryReference) {
            if (this.compound.primaryReferenceUrl) {
                return `<a href="${this.compound.primaryReferenceUrl}" target="_blank" ` +
                    `rel="noopener noreferrer">${this.compound.primaryReference}</a>`;
            }
            return this.compound.primaryReference;
        }

        return '';
    }

    mounted() {
        // Just to avoid an NPE if an error occurs when rendering this component.
        this.compoundName = this.compound ? this.compound.compoundName : 'unknown';
    }

    onImageClicked() {
        this.$store.commit('setLightboxImage', this.compoundImageUrl);
        this.$store.commit('setLightboxTitle', this.compoundName);
    }
}
</script>

<style lang="less">
@import '../styles/app-variables';

.compound-details-card {

    .compound-details-card-title {
        padding-bottom: 0;
    }

    img {
        vertical-align: middle;

        &.compound-image {
            cursor: pointer;
            transition: transform @transition-time;

            &:hover {
                transform: scale(1.1);
            }
        }
    }

    .compound-details-table {

        display: table;

        .compound-details-table-row {

            display: table-row;

            .compound-details-table-cell {

                display: table-cell;
                padding: 0 2rem 0 0;

                &.compound-details-table-cell-header {
                    font-weight: bold;
                    white-space: nowrap;
                }
            }
        }
    }
}
</style>
