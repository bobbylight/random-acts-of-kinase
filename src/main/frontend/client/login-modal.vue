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

                <form class="ui form">

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
            <div class="ui positive right button" :class="{ disabled: !user || !password }" @click="login">
                Log In
            </div>
            <div class="ui right button">
                Cancel
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';

@Component
export default class LoginModal extends Vue {

    private user: string = '';
    private password: string = '';

    login() {
        console.log('Attempting to log in as ' + this.user + ', ' + this.password);
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
