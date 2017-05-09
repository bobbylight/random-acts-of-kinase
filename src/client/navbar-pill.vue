<template>
    <button class="ui button pill" v-on:click="navigate">{{label}}</button>
</template>

<script>
export default {
    name: 'navbar-pill',
    props: {
        label: {
            type: String,
            required: true
        }
    },
    methods: {
        navigate: function() {
            this.$router.push({ name: 'compound', params: { id: this.label }});
        }
    }
};
</script>

<style>

    /* pill styles are essentially copied from ui inverted menu */
    .ui.button.pill {
        background: rgba(255, 255, 255, 0.08);
        color: rgba(255,255,255,.9);
        font-weight: 400;
        padding-top: 0.5rem;
        padding-bottom: 0.5rem;
        border-radius: 0.8rem;
    }
    .ui.button.pill:hover {
        background: rgba(255, 255, 255, 0.15);
        color: #fff;
    }
</style>
