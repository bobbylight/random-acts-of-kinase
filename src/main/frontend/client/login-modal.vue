<template>
    <div class="ui tiny modal">
        <i class="close icon"></i>
        <div class="header">
            Log In
        </div>
        <div class="content">
            <div class="description">

                <div class="details">
                    Sign in to get even more out of your RAK experience.
                </div>

                <form class="ui form" id="login-form">

                    <div class="field">
                        <label>User name</label>
                        <input type="text" placeholder="User name" v-model="user">
                    </div>
                    <div class="field">
                        <label>Password</label>
                        <input type="password" placeholder="Password" v-model="password">
                    </div>
                </form>
            </div>
        </div>
        <div class="actions">
            <button type="submit" form="login-form" class="ui positive right button" :class="{ disabled: !user || !password }">
                Log In
            </button>
            <div class="ui negative right button">
                Cancel
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop, Watch } from 'vue-property-decorator';
import $ from 'jquery';
import restApi from './rest-api';
import { AxiosError } from 'axios';
import { UserRep } from './rak';

const HIDDEN: string = 'hidden';

@Component
export default class LoginModal extends Vue {

    private user: string = '';
    private password: string = '';

    @Prop({ required: true })
    private visible: boolean;

    login(): boolean {
        console.log('Attempting to log in as ' + this.user + ', ' + this.password);
        restApi.login(this.user, this.password)
            .then((response: UserRep) => {
                console.log('Login success!!!!');
                this.password = ''; // Clear password, but must keep user for toaster
                this.$store.commit('setUser', this.user);
                this.$emit(HIDDEN);
                $(this.$el).modal('hide');
                (this as any).$toasted.success(`Welcome back, ${this.user}!`, {
                    position: 'bottom-right',
                    duration: 5000
                });
            })
            .catch((response: AxiosError) => {
                console.log('Login failure :(:(:(');
                if (response.response && response.response.status === 401) {
                    alert('Username or password is invalid');
                }
                else {
                    alert('An unknown error occurred');
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

    @Watch('visible')
    private onVisibilityChange(newValue: boolean) {
        if (newValue) {

            this.user = this.password = '';
            const onCancel: Function = this.onCancel.bind(this);

            $(this.$el).modal({
                onApprove: this.login.bind(this),
                onDeny: onCancel,
                onHide: onCancel
            }).modal('show');
        }
    }
}
</script>

<style lang="less">
.description {
    .details {
        padding-bottom: 2rem;
    }
}
</style>
