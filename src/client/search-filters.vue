<template>
    <div class="search-filters">

        <div>Search by compound:</div>
        <div class="ui grid">
            <div class="sixteen wide column">
                <search-field v-model="filters.inhibitor"
                              place-holder="Inhibitor"></search-field>
            </div>
        </div>

        <div style="margin-top: 1rem">Or by kinase and activity:</div>
        <div class="ui grid">
            <div class="twelve wide column">
                <!--<search-field type="kinase" v-model="filters.kinase"-->
                <!--place-holder="Kinase"></search-field>-->
                <lazy-dropdown v-model="filters.kinase"
                               name="kinase" url="api/kinases?filter={query}"></lazy-dropdown>
            </div>

            <div class="four wide column">
                <search-field type="number" v-model="filters.activity" numeric="true"
                              place-holder="Remaining activity" label="%"></search-field>
            </div>

        </div>
    </div>
</template>

<script>
import SearchField from './search-field.vue';
import LazyDropdown from './lazy-dropdown.vue';

export default {
    components: {
        SearchField,
        LazyDropdown
    },
    props: {
        filters: {
            type: Object,
            required: true
        }
    },
    data() {
        return {};
    }
};
</script>

<style lang="less">
</style>
