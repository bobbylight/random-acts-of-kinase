<template>
    <div class="ui input search-field" v-bind:class="{ 'right labeled': label }">
        <input :type="type" :placeholder="placeHolder" v-bind:class="{ 'right-aligned': !!numeric }"
               :value="value" step="0.1" min="0.1" max="100" @input="possiblyValidate($event)">
        <div class="ui label" v-bind:style="{ display: labelDisplay }">{{label}}</div>
    </div>
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

    get labelDisplay() {
        return this.label ? 'inherit' : 'none';
    }

    possiblyValidate($event: Event) {

        const value: any = ($event.target as HTMLInputElement).value;
        const input: $ = $(this.$el);

        if ($($event.target).is(':invalid')) {
            input.addClass('error');
            return;
        }

        input.removeClass('error');
        this.$emit('input', value);
    }
}
</script>

<style lang="less">
    .ui.input.search-field {
        width: 100%;

        .right-aligned {
            text-align: right;
        }
    }
    .ui.input.search-field.error {
        .ui.label {
            border: 1px solid #e0b4b4; // Matches input's error border
        }
    }
</style>
