<template>
    <v-dialog v-model="visible" max-width="700px">
        <v-card>

            <v-card-title>
                <div class="headline">Edit Compound {{compoundCopy.compoundName}}</div>
            </v-card-title>

            <v-card-text class="container grid-list-md">
                <v-form ref="form" v-model="valid">

                    <v-flex xs12>
                        <v-text-field type="text" label="Chemotype"
                                      ref="chemotypeField"
                                      @input="dirty = true"
                                      autocomplete="off"
                                      v-model="compoundCopy.chemotype"></v-text-field>
                    </v-flex>

                    <v-layout row>

                        <v-flex xs6>
                            <v-text-field type="number" label="s(10)" class="right-aligned"
                                          :rules="s10ValidationRules" v-model="compoundCopy.s10"
                                          @input="dirty = true"
                                          autocomplete="off"
                                          :step="0.01" :min="0" :max="1">
                            </v-text-field>
                        </v-flex>

                        <v-flex xs6>
                            <v-text-field type="number" label="Solubility" class="right-aligned"
                                          :rules="solubilityValidationRules"
                                          v-model="compoundCopy.solubility"
                                          @input="dirty = true"
                                          autocomplete="off"
                                          :step="0.1" :min="0" :max="10000" suffix="µg/mL">
                            </v-text-field>
                        </v-flex>
                    </v-layout>

                    <v-flex xs12>
                        <v-text-field type="text" label="SMILES"
                                      @input="dirty = true"
                                      autocomplete="off"
                                      v-model="compoundCopy.smiles"></v-text-field>
                    </v-flex>

                    <v-flex xs12>
                        <v-text-field type="text" label="Source"
                                      @input="dirty = true"
                                      autocomplete="off"
                                      v-model="compoundCopy.source"></v-text-field>
                    </v-flex>

                    <v-flex xs12>
                        <v-text-field type="text" label="Primary Reference"
                                      @input="dirty = true"
                                      :rules="primaryReferenceRules"
                                      autocomplete="off"
                                      v-model="compoundCopy.primaryReference"></v-text-field>
                    </v-flex>

                    <v-flex xs12>
                        <v-text-field type="text" label="Primary Reference URL"
                                      @input="dirty = true"
                                      :rules="primaryReferenceUrlRules"
                                      autocomplete="off"
                                      v-model="compoundCopy.primaryReferenceUrl"></v-text-field>
                    </v-flex>

                    <v-layout row align-center>

                        <v-flex xs3>
                            <v-switch
                                class="edit-compound-hidden-switch"
                                :label="`${compoundCopy.hidden ? 'Hidden' : 'Not hidden'}`"
                                @change="dirty = true"
                                v-model="compoundCopy.hidden"></v-switch>
                        </v-flex>
                        <v-flex xs9>
                            <transition name="fade">
                                <v-chip disabled color="warning" v-if="compoundCopy.hidden">
                                    <v-icon class="pr-1">warning</v-icon>
                                    Hidden compounds won't appear in search results except for admins
                                </v-chip>
                            </transition>
                        </v-flex>
                    </v-layout>
                </v-form>
            </v-card-text>

            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary"
                       :disabled="!dirty || !valid"
                       @click="onSubmit">Update</v-btn>
                <v-btn text
                       :disabled="!dirty"
                       @click="resetForm">Reset</v-btn>
                <v-btn text color="primary"
                       @click="visible = false">Cancel</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop, Watch } from 'vue-property-decorator';
import restApi from './rest-api';
import { Compound, ErrorResponse } from './rak';
import Toaster from './toaster';

// tslint:disable-next-line
const URL_REGEX: RegExp = /^(https?:\/\/(?:www\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\.[^\s]{2,}|www\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\.[^\s]{2,}|https?:\/\/(?:www\.|(?!www))[a-zA-Z0-9]\.[^\s]{2,}|www\.[a-zA-Z0-9]\.[^\s]{2,})$/;

@Component
export default class EditCompoundModal extends Vue {

    @Prop({ required: true })
    show: boolean;

    @Prop({ required: true })
    compound: Compound;

    private compoundCopy: Compound = this.createCompoundCopy();

    valid: boolean = true;

    private dirty: boolean = false;

    private readonly s10ValidationRules: any[] = [

        (value: string) => {
            return EditCompoundModal.isEmpty(value) || !isNaN(parseFloat(value)) || 'Must be a number';
        },

        (value: string) => {
            if (!parseFloat(value)) {
                return true; // Will be caught by the rule above
            }
            return +value >= 0 && +value <= 1 || 'Must be between 0 and 1';
        }
    ];

    private readonly solubilityValidationRules: any[] = [

        (value: string) => {
            return EditCompoundModal.isEmpty(value) || !isNaN(parseFloat(value)) || 'Must be a number';
        },

        (value: string) => {
            if (!parseFloat(value)) {
                return true; // Will be caught by the rule above
            }
            return +value >= 0 && +value <= 10000 || 'Must be between 0 and 10000';
        }
    ];

    foo() {
        console.log('foo called');
    }

    private createCompoundCopy(): Compound {

        if (!this.compound) {
            return {
                compoundName: '',
                chemotype: '',
                s10: '',
                solubility: undefined,
                smiles: '',
                source: '',
                primaryReference: '',
                primaryReferenceUrl: '',
                hidden: false
            };
        }

        return JSON.parse(JSON.stringify(this.compound));
    }

    private static isEmpty(text: string): boolean {
        // text might be a number
        return !text || !text.trim || !text.trim().length;
    }

    private readonly primaryReferenceRules: Function[] = [
        (primaryReference: string) => {
            return !primaryReference || // Server does not send down null values
                primaryReference.length < 768 ||
                'Invalid Primary Reference';
        }
    ];

    private readonly primaryReferenceUrlRules: Function[] = [
        (primaryReferenceUrl: string) => {
            return !primaryReferenceUrl || // Server does not send null values
                (primaryReferenceUrl.length < 2083 && URL_REGEX.test(primaryReferenceUrl)) ||
                'Invalid Primary Reference URL';
        }
    ];

    get visible(): boolean {
        return this.show;
    }

    set visible(newValue: boolean) {
        if (!newValue) {
            this.$emit('close');
        }
    }

    @Watch('compound')
    onCompoundChanged() {
        this.compoundCopy = this.createCompoundCopy();
    }

    /**
     * When this modal is told to show itself, focus the email name field.
     *
     * @param {boolean} newValue Whether to show or hide ourselves.
     */
    @Watch('show')
    private onShowChanged(newValue: boolean) {
        if (newValue) {
            this.resetForm();
            this.$nextTick(() => {
                const firstInput: Vue = this.$refs.chemotypeField as Vue;
                (firstInput.$el as HTMLInputElement).focus();
            });
        }
    }

    onSubmit() {

        restApi.updateCompound(this.compoundCopy)
            .then((newCompound: Compound) => {
                this.$emit('updated', newCompound);
                this.$emit('close');
                Toaster.success('Compound updated');
                this.visible = false;
            })
            .catch((errorResponse: ErrorResponse) => {
                Toaster.error('An error occurred updating the compound.');
            });
    }

    resetForm() {
        // reset() clears the form, it doesn't reset it:
        // https://github.com/vuetifyjs/vuetify/issues/3316
        // this.$refs.from.reset();
        this.compoundCopy = this.createCompoundCopy();
        this.dirty = false;
    }
}
</script>

<style lang="less">
.edit-compound-hidden-switch {

    display: inline-block;

    .v-messages {
        display: none;
    }
}
</style>
