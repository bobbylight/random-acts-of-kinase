<template>
    <span :id="spanId" :class="classes">
        <v-select
            ref="select"
            :id="id"
            :label="label"
            :placeholder="placeholder"
            :loading="loading"
            :prepend-icon="icon"
            :clearable="true"
            autocomplete
            browser-autocomplete="off"
            :items="items"
            :item-text="responseLabelField"
            :item-value="responseValueField"
            :search-input.sync="search"
            :value="curValue"
            @input="fireUpdateEvent($event)"
        ></v-select>
    </span>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop, Watch } from 'vue-property-decorator';
import axios, { AxiosResponse } from 'axios';
import debounce from 'debounce';

/**
 * A simple key/value pair map for storing query parameters.
 */
export interface QueryParams {
    [ key: string ]: string;
}

@Component
export default class LazyDropdown extends Vue {

    /**
     * "value" facilitates v-model support
     */
    @Prop({ required: true })
    value: any;

    @Prop({ required: true })
    url: string;

    @Prop({ required: true })
    filterParamName: string;

    @Prop({ 'default': 'label' })
    responseLabelField: string;

    @Prop({ 'default': 'value' })
    responseValueField: string;

    @Prop()
    queryParams: QueryParams;

    @Prop({ required: true })
    id: string;

    @Prop()
    icon: string;

    @Prop()
    label: string;

    @Prop()
    placeholder: string;

    /**
     * If defined, this function is called to transform the response into a new format.
     * Useful if the service being called doesn't return an array of objects, for example.
     */
    @Prop()
    transform: (response: any) => any[];

    @Prop()
    focus: boolean | string;

    @Prop({ 'default': 300 })
    debounceMillis: number;

    @Prop()
    classes: string;

    curValue: string = this.value;

    items: any[] = [];

    loading: boolean = false;

    search: string | null = null;

    get spanId() {
        return `${this.id}-span`;
    }

    created() {
        if (this.value) {
            const item: any = {};
            item[this.responseLabelField] = this.value;
            item[this.responseValueField] = this.value;
            this.items.push(item);
        }
        this.runQuery = debounce(this.runQuery, this.debounceMillis);
    }

    mounted() {
        // if (this.focus === 'true' || !!this.focus) {
        //     (this.$refs.select as HTMLElement).focus();
        // }
    }

    @Watch('search')
    onSearchChanged(newValue: string, oldValue: string) {
        console.log('onSearchChanged called');
        newValue = newValue || '';
        this.runQuery(newValue);
    }

    private static clone<T>(obj: T): T {
        return JSON.parse(JSON.stringify(obj));
    }

    /**
     * Fires an "input" event stating our value has changed.  Part of implementing v-model for this component.
     */
    fireUpdateEvent(newValue: any) {
        this.$emit('input', newValue);
    }

    private runQuery(query: string) {

        this.loading = true;

        const queryParams: QueryParams = LazyDropdown.clone(this.queryParams);
        queryParams[this.filterParamName] = query;

        axios.get(this.url, { params: queryParams })
            .then((response: AxiosResponse<any>) => {
                this.items = this.transform ? this.transform(response.data) : response.data;
                this.loading = false;
            })
            .catch((error: AxiosResponse<any>) => {
                this.loading = false;
            });
    }
}
</script>

<style lang="less">
</style>
