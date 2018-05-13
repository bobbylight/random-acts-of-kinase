<template>
    <v-card>
        <v-card-title>
            <h3 class="headline">{{compoundName}}</h3>
        </v-card-title>

        <v-card-text>
            <v-container grid-list-md>
                <v-layout row wrap>

                    <v-flex xs3>
                        <img :src="compoundImageUrl" width="100" height="100">
                    </v-flex>

                    <v-flex xs9>

                        <div class="compound-details-table">

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
                    <div>Lorem</div>
                    <div>ipsum...</div>
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
import {Compound, ErrorResponse} from "./rak";
import Toaster from "./toaster";

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
                return `<a href="${this.primaryReferenceUrl}">${this.primaryReference}</a>`;
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
}
</script>

<style lang="less">
.compound-details-table {

    display: table;

    .compound-details-table-row {

        display: table-row;

        .compound-details-table-cell {

            display: table-cell;
            padding: 0 1rem 0 0;

            &.compound-details-table-cell-header {
                font-weight: bold;
            }
        }
    }
}
</style>
