<template>
    <div>
        <v-text-field type="number" :label="label" class="search-field right-aligned"
                      :rules="numericValidationRules" v-model="fieldValue" @input="onFieldValueChanged"
                      autocomplete="off" hide-details
                      :step="step" :min="min" :max="max" suffix="%">
        </v-text-field>
        <activity-or-kd-toggle-button ref="dropdown"></activity-or-kd-toggle-button>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Watch } from 'vue-property-decorator';
import ActivityOrKdToggleButton from './activity-or-kd-toggle-button.vue';

@Component({ components: { ActivityOrKdToggleButton } })
export default class ActivityOrKdField extends Vue {

    get filters() {
        return this.$store.state.filters;
    }

    private label: string = 'Remaining activity';
    private fieldValue: string = '';

    private step: number = 0.1;
    private min: number = 0.1;
    private max: number = 100;

    private readonly numericValidationRules: any[] = [

        (value: string) => {
            return ActivityOrKdField.isEmpty(value) || !isNaN(parseFloat(value)) || 'Must be a number';
        },

        (value: string) => {
            if (!parseFloat(value)) {
                return true; // Will be caught by the rule above
            }
            const max: number = this.filters.activityOrKd === 'percentControl' ? 100 : 10000;
            return +value >= 0 && +value <= max || `Must be between 0 and ${max}`;
        }
    ];

    private static isEmpty(text: string): boolean {
        // text might be a number
        return !text || !text.trim || !text.trim().length;
    }

    mounted() {

        // Hijack the suffix content and replace it with a custom widget
        const dropdown: Vue = this.$refs.dropdown as Vue;
        dropdown.$el.parentElement!.removeChild(dropdown.$el);

        const suffixDiv: HTMLElement = this.$el.querySelector('.v-text-field__suffix') as HTMLElement;
        suffixDiv.innerHTML = '';
        suffixDiv.appendChild(dropdown.$el);
    }

    onFieldValueChanged(newValue: string) {
        const storeCommitFunction: string =
            this.filters.activityOrKd === 'percentControl' ? 'setFilterByActivity' : 'setFilterByKd';
        this.$store.commit(storeCommitFunction, newValue);
    }

    @Watch('filters.kinase')
    onKinaseChanged(newValue: string) {
        // Populate with default value when the first kinase is selected
        if (!this.fieldValue.length) {
            this.updateBasedOnActivityOrKd();
        }
    }

    @Watch('filters.activityOrKd')
    onActivityOrKdChanged() {
        this.updateBasedOnActivityOrKd();
    }

    private updateBasedOnActivityOrKd() {
        if (this.filters.activityOrKd === 'percentControl') {
            this.label = 'Remaining activity';
            this.fieldValue = this.filters.activity;
            this.step = this.min = 0.1;
            this.max = 100;
        }
        else {
            this.label = 'Kd';
            this.fieldValue = this.filters.kd;
            this.step = this.min = 1;
            this.max = 10000;
        }
    }
}
</script>

<style lang="less">
.search-field {
    width: 100%;

    &.right-aligned {
        input {
            text-align: right;
        }
    }
}
</style>
