<template>
    <div class="partner-badge-wrapper">
        <a :href="partner.url" target="_blank" rel="noopener noreferrer">
            <img class="partner-image" :src="getImageUrl(partner.image)"
                 alt="Partner logo">
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
    private readonly partner: Partner;

    getImageUrl(imageFileName: string): string {
        return `/img/partners/${imageFileName}`;
    }
}
</script>

<style lang="less">
@import '../styles/app-variables';

.partner-badge-wrapper {

    height: 300px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 1rem;

    img.partner-image {
        width: 300px;
        transition: transform @transition-time;

        &:hover {
            transform: scale(1.1);
        }
    }
}
</style>
