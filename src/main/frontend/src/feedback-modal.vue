<template>
    <v-dialog v-model="visible" max-width="640px" :persistent="true">
        <v-card>

            <v-card-title>
                <div>Feedback</div>
                <div class="feedback-subtitle">
                    Have suggestions for improvements or new features?  Found a bug?  Provide feedback here.
                    Your email is optional, and allows us to follow up with you if we need more information.
                </div>
            </v-card-title>

            <v-card-text>

                <v-flex xs12>
                    <v-text-field type="text" label="E-mail (optional)"
                                  ref="emailField"
                                  :rules="emailRules"
                                  v-model="email"></v-text-field>
                </v-flex>

                <v-flex xs12>
                    <v-text-field type="text" label="Summary"
                                  :rules="summaryRules"
                                  v-model="summary"></v-text-field>
                </v-flex>

                <v-flex xs12>
                    <v-textarea label="Details"
                                :rules="detailsRules"
                                :counter="8000"
                                v-model="details"></v-textarea>
                </v-flex>
            </v-card-text>

            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="primary"
                       :disabled="submitButtonDisabled()"
                       @click="onSubmit">Submit</v-btn>
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
import { Feedback } from './rak';
import Toaster from './toaster';

@Component
export default class FeedbackModal extends Vue {

    @Prop({ required: true })
    show: boolean;

    private email: string = '';
    private summary: string = '';
    private details: string = '';

    // tslint:disable
    readonly EMAIL_REGEX: RegExp =
        /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

    private emailRules: Function[] = [
        (email: string) => {
            return email.length === 0 || this.EMAIL_REGEX.test(email) ||
                'Invalid e-mail';
        }
    ];

    private summaryRules: Function[] = [
        (value: string) => {
            return value.length <= 128 || 'Value is too long';
        }
    ];

    private detailsRules: Function[] = [
        (value: string) => {
            return value.length <= 8000 || 'Value is too long';
        }
    ];

    get visible() {
        return this.show;
    }

    set visible(newValue: boolean) {
        if (!newValue) {
            this.$emit('close');
        }
    }

    /**
     * When this modal is told to show itself, focus the email name field.
     *
     * @param {boolean} newValue Whether to show or hide ourselves.
     */
    @Watch('show')
    private onShowChanged(newValue: boolean) {
        if (newValue) {
            this.$nextTick(() => {
                (this.$refs.emailField as HTMLInputElement).focus();
            });
        }
        else {
            this.email = this.summary = this.details = '';
        }
    }

    onSubmit() {

        const feedback: Feedback = {
            email: this.email,
            title: this.summary,
            body: this.details
        };

        restApi.createFeedback(feedback)
            .then(() => {
                this.email = this.summary = this.details = '';
                this.$emit('close');
                Toaster.success('Feedback submitted, thanks!');
                this.visible = false;
            });
    }

    submitButtonDisabled() {
        return this.summary.length <= 3 || this.summary.length > 128 ||
            this.details.length === 0 || this.details.length > 8000;
    }
}
</script>

<style lang="less">
.feedback-subtitle {
    font-size: initial;
    font-weight: initial;
    line-height: 1.5;
}
</style>
