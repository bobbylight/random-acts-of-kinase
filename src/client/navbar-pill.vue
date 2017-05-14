<template>
    <div class="navbar-pill-parent">
        <button class="ui button pill" v-bind:class="{ active: isActiveTab() }"
                v-on:click="navigate">{{compound}}
            <i class="fa fa-times close-icon" aria-hidden="true" v-on:click="close"></i>
        </button>
    </div>
</template>

<script>
export default {
    name: 'navbar-pill',
    props: {
        compound: {
            type: String,
            required: true
        }
    },
    methods: {

        isActiveTab() {
            const pathRegex = new RegExp(this.compound + '$');
            return !!this.$route.fullPath.match(pathRegex);
        },

        navigate() {
            this.$router.push({ name: 'compound', params: { id: this.compound }});
        },

        close(e) {
            this.$emit('close', this.compound);
            e.preventDefault();
            e.stopPropagation();
        }
    }
};
</script>

<style lang="less">

    .navbar-pill-parent {
        position: relative;
    }

    /* pill styles are essentially copied from ui inverted menu */
    .ui.button.pill {
        background: rgba(255, 255, 255, 0.08);
        color: rgba(255,255,255,.9);
        font-weight: 400;
        padding-top: 0.5rem;
        padding-bottom: 0.5rem;
        border-radius: 0.8rem;
        transition: color .5s ease,
                    background-color .5s ease;

        &:hover, &.active {
            background: rgba(255, 255, 255, 0.15);
            color: #fff;
        }

        .close-icon {
            color: gray;
            font-size: 1.3rem;
            position: absolute;
            top: -6px;
            right: 0;
            transition: color .5s ease;
            &:hover {
                color: white;
            }
        }
    }
</style>
