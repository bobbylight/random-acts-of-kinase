<template>
    <v-container grid-list-md class="page-wrapper">

        <v-layout row wrap class="partners-wrapper">

            <section-header>Partners</section-header>

            <v-flex xs6 v-for="partner in partners" :key="partner.id">
                {{partner.name}}
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import SectionHeader from './header.vue';
import { PagedDataRep, Partner } from './rak';
import restApi from './rest-api';

@Component({ components: { SectionHeader } })
export default class Partners extends Vue {

    private partners: Partner[] = [];

    private loadPartners() {

        console.log('Loading partners...');

        restApi.getPartners()
            .then((partners: PagedDataRep<Partner>) => {
                this.partners = partners.data;
            });
    }

    mounted() {
        this.loadPartners();
    }
}
</script>

<style lang="less">
@import '../styles/app-variables';

.blog-wrapper {

    .blog-post:not(:first-of-type) {
        margin-top: @card-vertical-spacing;
    }

    .blog-page-footer {
        margin-top: 1rem;
        text-align: right;
    }
}
</style>
