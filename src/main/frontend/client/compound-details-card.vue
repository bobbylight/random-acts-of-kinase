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
                            <img :src="compoundImageUrl"
                                 class="compound-image"
                                 @click="onImageClicked"
                                 width="200" height="200"
                                 slot="activator">
                            <span>Click to enlarge</span>
                        </v-tooltip>
                    </v-flex>

                    <v-flex xs9 pt-0 pl-5>

                        <div class="compound-details-table">

                            <div class="compound-details-table-row">
                                <div class="compound-details-table-cell compound-details-table-cell-header">Compound:</div>
                                <div class="compound-details-table-cell">{{compoundName}}</div>
                            </div>

                            <div class="compound-details-table-row">
                                <div class="compound-details-table-cell compound-details-table-cell-header">Chemotype:</div>
                                <div class="compound-details-table-cell">{{chemotype}}</div>
                            </div>

                            <div class="compound-details-table-row">
                                <div class="compound-details-table-cell compound-details-table-cell-header">s(10):</div>
                                <div class="compound-details-table-cell">{{s10}}</div>
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
    private compoundName: string;

    private chemotype: string | null | undefined = null;
    private s10: string | null | undefined = null;
    private primaryReference: string | null | undefined = null;
    private primaryReferenceUrl: string | null | undefined = null;

    get compoundImageUrl() {
        return `api/compounds/images/${this.compoundName}`;
    }

    get reference() {

        if (this.primaryReference) {
            if (this.primaryReferenceUrl) {
                return `<a href="${this.primaryReferenceUrl}" target="_blank" ` +
                    `rel="noopener noreferrer">${this.primaryReference}</a>`;
            }
            return this.primaryReference;
        }

        return '';
    }

    mounted() {

        restApi.getCompound(this.compoundName)
            .then((compound: Compound) => {
                this.chemotype = compound.chemotype;
                this.s10 = compound.s10;
                this.primaryReference = compound.primaryReference || compound.primaryReferenceUrl;
                this.primaryReferenceUrl = compound.primaryReferenceUrl;
            })
            .catch((errorResponse: ErrorResponse) => {
                Toaster.error('Error retrieving compound information');
            });
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
                transform: scale(1.2);
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
                }
            }
        }
    }
}
</style>
