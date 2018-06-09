<template>
    <div class="partner-badge-wrapper">
        <a :href="partner.url" target="_blank" rel="noopener noreferrer">
            <img :src="getImageUrl(partner.image)" style="width: 300px">
        </a>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';
import { Partner } from './rak';

@Component
export default class PartnerBadge extends Vue {

    @Prop({ required: true })
    private partner: Partner;

    getImageUrl(imageFileName: string): string {
        return `/img/partners/${imageFileName}`;
    }
}
</script>

<style lang="less">
.partner-badge-wrapper {

    height: 300px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 1rem;
}
</style>
