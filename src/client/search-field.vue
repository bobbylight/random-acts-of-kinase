<template>
    <div class="ui input">
        <input type="text" placeholder="Search..." v-model="filter" v-on:input="valueChanged($event)">
    </div>
</template>

<script>
export default {

    name: 'searchField',

    props: {
        initialFilter: {
            type: String,
            required: true
        }
    },

    data: function() {
        return {
            filter: this.initialFilter,
            hardCoded: 'Hello'
        };
    },

    methods: {
        valueChanged($event) {
            this.$emit('change', $event.target.value);
        }
    }
};
</script>

<style scoped>
</style>
