<template>
    <v-text-field :type="type" :placeholder="placeHolder" class="search-field"
                  :class="{ 'right-aligned': !!numeric }" :rules="validationRules"
                  :value="value" step="0.1" min="0.1" max="100" @input="publishValueChange(value)"></v-text-field>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';
import $ from 'jquery';

@Component
export default class SearchField extends Vue {

    @Prop({ default: 'text' })
    type: string;

    @Prop({ required: true })
    value: string; // Special prop received from v-model

    @Prop({ required: true })
    placeHolder: string;

    @Prop()
    label: string;

    @Prop()
    numeric: string;

    private static isEmpty(text: string): boolean {
        return !text.trim().length;
    }

    get labelDisplay() {
        return this.label ? 'inherit' : 'none';
    }

    publishValueChange(value: string) {
        this.$emit('input', value);
    }

    get validationRules(): any[] {

        const rules: any = [];

        if (this.numeric === 'true') {
            rules.push((value: string) => {
                return SearchField.isEmpty(value) || !isNaN(parseFloat(value)) || 'Must be a number';
            });
            rules.push((value: string) => {
                if (!parseFloat(value)) {
                    return true; // Will be caught by the rule above
                }
                return +value >= 0 && +value <= 100 || 'Must be between 0 and 100';
            });
        }

        return rules;
    }
}
</script>

<style lang="less">
    .search-field {
        width: 100%;

        &.right-aligned {
            text-align: right;
        }
    }
    .ui.input.search-field.error {
        .ui.label {
            border: 1px solid #e0b4b4; // Matches input's error border
        }
    }
</style>
