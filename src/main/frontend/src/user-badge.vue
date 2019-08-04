<template>
    <v-chip small disabled>
        <v-avatar>
            <img src="https://randomuser.me/api/portraits/men/35.jpg" alt="trevor">
        </v-avatar>
        <span class="user-badge-user-name">{{user}}</span>
    </v-chip>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';

@Component
export default class UserBadge extends Vue {

    @Prop({ required: true })
    private readonly user: string;
}
</script>

<style lang="less">
.user-badge-user-name {
    font-style: normal;
}
</style>
