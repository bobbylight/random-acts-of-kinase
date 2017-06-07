<template>
    <div class="ui input search-field" v-bind:class="{ 'right labeled': label }">
        <input :type="type" :placeholder="placeHolder" v-bind:class="{ 'right-aligned': !!numeric }"
               :value="value" step="0.1" min="0.1" max="100" @input="possiblyValidate($event)">
        <div class="ui label" v-bind:style="{ display: labelDisplay }">{{label}}</div>
    </div>
</template>

<script>
export default {

    name: 'searchField',

    props: {
        type: {
            type: String,
            required: false,
            default: 'text'
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
        },
        numeric: {
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
    },

    methods: {

        possiblyValidate($event) {

            const value = $event.target.value;
            const input = $(this.$el);

            if ($($event.target).is(':invalid')) {
                input.addClass('error');
                return;
            }

            input.removeClass('error');
            this.$emit('input', value);
        }
    }
};
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
