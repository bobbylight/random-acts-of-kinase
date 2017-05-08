<template>
    <div class="ui input search-field" v-bind:class="{ 'right labeled': label }">
        <input type="text" :placeholder="placeHolder" :value="value" @input="$emit('input', $event.target.value)">
        <div class="ui label" v-bind:style="{ display: labelDisplay }">{{label}}</div>
    </div>
</template>

<script>
export default {

    name: 'searchField',

    props: {
        type: {
            type: String,
            required: true
        },
        value: { // Special prop received from v-model
            type: String,
            required: true
        },
        placeHolder: {
            type: String,
            required: true
        },
        label: {
            type: String,
            required: false
        }
    },

    computed: {
        labelDisplay: function() {
            return this.label ? 'inherit' : 'none';
        }
    },

    data: function() {
        return {
        };
    }
};
</script>

<style scoped>
    .search-field {
        width: 100%;
    }
</style>
