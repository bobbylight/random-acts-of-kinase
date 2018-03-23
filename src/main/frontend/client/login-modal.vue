<template>
    <v-dialog v-model="visible" max-width="500px">
        <v-card>

            <v-card-title class="headline">Log In</v-card-title>

            <v-card-text>

                <div class="details">
                    Sign in to get even more out of your RAK experience.
                </div>

                <form id="login-form">
                    <v-text-field label="User name" v-model="user"></v-text-field>
                    <v-text-field label="Password" v-model="password"
                                  :append-icon="passwordVisible ? 'visibility' : 'visibility_off'"
                                  :append-icon-cb="() => (passwordVisible = !passwordVisible)"
                                  :type="passwordVisible ? 'password' : 'text'"></v-text-field>
                </form>
            </v-card-text>

            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="success" type="submit" @click="login()" :disabled="!user || !password">
                    Log In
                </v-btn>
                <v-btn @click="onCancel()">
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
import $ from 'jquery';
import restApi from './rest-api';
import { AxiosError } from 'axios';
import { UserRep } from './rak';
import Toaster from './toaster';

const HIDDEN: string = 'close';

@Component
export default class LoginModal extends Vue {

    private user: string = '';
    private password: string = '';
    private passwordVisible: boolean = true;

    @Prop({ required: true })
    private show: boolean;

    get visible() {
        return this.show;
    }

    set visible(newValue: boolean) {
        if (!newValue) {
            this.$emit('close');
        }
    }

    login(): boolean {
        console.log('Attempting to log in as ' + this.user + ', ' + this.password);
        restApi.login(this.user, this.password)
            .then((response: UserRep) => {
                console.log('Login success!!!!');
                this.password = ''; // Clear password, but must keep user for toaster
                this.$store.commit('setUser', this.user);
                this.$emit(HIDDEN);
                Toaster.success(`Welcome back, ${this.user}!`);
            })
            .catch((response: AxiosError) => {
                console.log('Login failure :(:(:(');
                if (response.response && response.response.status === 401) {
                    Toaster.error('You typed the wrong username or password');
                }
                else {
                    Toaster.error('An unknown error occurred');
                }
                console.error(response);
            })
            //.finally(() => {
            //    this.user = this.password = '';
            //})
        ;
        return false;
    }

    onCancel() {
        this.$emit(HIDDEN);
    }
}
</script>

<style lang="less">
.details {
    padding-bottom: 2rem;
}
</style>
