<template>
    <div>
        <v-text-field type="number" label="Remaining activity" class="search-field right-aligned"
                      :rules="numericValidationRules" v-model="fieldValue" @input="onFieldValueChanged"
                      step="0.1" min="0.1" max="100" suffix="%">
        </v-text-field>
        <activity-or-kd-toggle-button ref="dropdown"></activity-or-kd-toggle-button>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';
import ActivityOrKdToggleButton from './activity-or-kd-toggle-button.vue';

@Component({ components: { ActivityOrKdToggleButton } })
export default class ActivityOrKdField extends Vue {

    /**
     * "value" facilitates v-model support
     */
    @Prop({ required: true })
    value: any;

    private fieldValue: string = '';

    private suffix: string = '%';

    private numericValidationRules: any[] = [

        (value: string) => {
            return ActivityOrKdField.isEmpty(value) || !isNaN(parseFloat(value)) || 'Must be a number';
        },

        (value: string) => {
            if (!parseFloat(value)) {
                return true; // Will be caught by the rule above
            }
            return +value >= 0 && +value <= 100 || 'Must be between 0 and 100';
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

        const suffixDiv: HTMLElement = this.$el.querySelector('.input-group--text-field__suffix') as HTMLElement;
        suffixDiv.innerHTML = '';
        suffixDiv.appendChild(dropdown.$el);
    }

    onFieldValueChanged(newValue: string) {
        this.$emit('input', newValue);
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
