<template>
    <div class="expected-columns">
        <div v-for="column in columnNames" class="expected-column-name">
            <span v-html="column"/>
        </div>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';

@Component({ components: { } })
export default class ExpectedColumnListing extends Vue {

    @Prop({ required: true })
    private readonly columnNames: string[];
}
</script>

<style lang="less">
.expected-columns {

    text-align: center;
    padding-top: 3rem;
    padding-bottom: 3rem;
    font-size: smaller;

    .expected-column-name {
        border: 1px solid gray;
        background: #fafafa;
        display:inline;
        padding: 0.5rem 1rem;

        &:first-child {
            border-top-left-radius: 3px;
            border-bottom-left-radius: 3px;
        }
        &:last-child {
            border-top-right-radius: 2px;
            border-bottom-right-radius: 2px;
        }
        &:not(:last-child) {
            border-right: none;
        }

        &:hover {
            background: #f0f0f0;
        }
    }
}
</style>
