<template>
    <v-dialog v-model="visible" max-width="500px" @keydown.esc="onCancel">
        <v-card>

            <v-card-title class="headline">Log In</v-card-title>

            <v-card-text>

                <div class="details">
                    Sign in to get even more out of your RAK experience.
                </div>

                <form id="login-form" @submit.prevent="login">
                    <v-text-field label="User name" v-model="user" ref="userNameField"></v-text-field>
                    <v-text-field label="Password" v-model="password"
                                  :append-icon="passwordVisible ? 'visibility' : 'visibility_off'"
                                  @click:append="passwordVisible = !passwordVisible"
                                  :type="passwordVisible ? 'password' : 'text'"></v-text-field>
                </form>
            </v-card-text>

            <v-card-actions>
                <v-spacer/>
                <v-btn color="primary" type="submit" form="login-form" :disabled="!user || !password">
                    Log In
                </v-btn>
                <v-btn text color="primary" @click="onCancel">
                    Cancel
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop, Watch } from 'vue-property-decorator';
import restApi from './rest-api';
import { ErrorResponse, UserRep } from './rak';
import Toaster from './toaster';

const HIDDEN: string = 'close';

@Component
export default class LoginModal extends Vue {

    private user: string = '';
    private password: string = '';
    private passwordVisible: boolean = true;

    @Prop({ required: true })
    private readonly show: boolean;

    get visible() {
        return this.show;
    }

    set visible(newValue: boolean) {
        if (!newValue) {
            this.$emit('close');
        }
    }

    login(): boolean {
        restApi.login(this.user, this.password)
            .then((response: UserRep) => {
                console.log('Login success!');
                this.password = ''; // Clear password, but must keep user for toaster
                this.$store.commit('setUser', this.user);
                this.$emit(HIDDEN);
                Toaster.success(`Welcome back, ${this.user}!`);
                this.user = this.password = '';
            })
            .catch((response: ErrorResponse) => {
                if (response.statusCode === 401) {
                    Toaster.error('You typed the wrong username or password');
                }
                else {
                    Toaster.error('An unknown error occurred');
                }
                console.error(response);
            })
        ;
        return false;
    }

    onCancel() {
        this.$emit(HIDDEN);
    }

    /**
     * When this modal is told to show itself, focus the user name field.
     *
     * @param {boolean} newValue Whether to show or hide ourselves.
     */
    @Watch('show')
    private onShowChanged(newValue: boolean) {
        if (newValue) {
            this.$nextTick(() => {
                (this.$refs.userNameField as HTMLInputElement).focus();
            });
        }
        else {
            this.user = this.password = '';
        }
    }
}
</script>

<style lang="less">
.details {
    padding-bottom: 2rem;
}
</style>
