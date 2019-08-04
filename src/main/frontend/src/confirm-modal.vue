<template>
    <v-dialog v-model="visible" max-width="500px" @keydown.esc="onCancel">
        <v-card>

            <v-card-title class="headline">{{title}}</v-card-title>

            <v-card-text>

                <div class="details">
                    {{details}}
                </div>
            </v-card-text>

            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="success" @click="onYes">
                    Yes
                </v-btn>
                <v-btn @click="onNo">
                    No
                </v-btn>
                <v-btn v-if="showCancel" @click="onCancel">
                    Cancel
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';

export const CONFIRM_RESULT: string = 'confirmResult';

/**
 * The possible values of the <code>CONFIRM_RESULT</code> event.
 */
export type ConfirmResult = 'yes' | 'no' | 'cancel';

@Component
export default class ConfirmModal extends Vue {

    @Prop({ required: true })
    private readonly show: boolean;

    @Prop({ required: true })
    private readonly title: string;

    @Prop({ required: true })
    private readonly details: string;

    @Prop({ default: false })
    private readonly showCancel: boolean;

    get visible() {
        return this.show;
    }

    set visible(newValue: boolean) {
        if (!newValue) {
            this.$emit(CONFIRM_RESULT, this.showCancel ? 'cancel' : 'no');
        }
    }

    onYes() {
        this.$emit(CONFIRM_RESULT, 'yes');
    }

    onNo() {
        this.$emit(CONFIRM_RESULT, 'no');
    }

    onCancel() {
        this.$emit(CONFIRM_RESULT, 'cancel');
    }
}
</script>

<style lang="less">

</style>
