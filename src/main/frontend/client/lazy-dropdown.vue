<template>
    <select :name="name" class="ui fluid search selection dropdown">
    </select>
</template>

<script>
export default {
    props: {
        name: {
            type: String,
            required: true
        },
        value: { // Special prop received from v-model
            type: String,
            required: true
        },
        url: {
            type: String,
            required: true
        }
    },
    mounted() {

        const dropdown = $(this.$el);

        dropdown.dropdown({
            saveRemoteData: false,
            apiSettings: {
                url: this.url,
                cache: false
            }
        });

        const vm = this;
        dropdown.on('change', function(e) {
            console.log('Changed to: ' + e.target.value);
            vm.$emit('input', this.value);
        });
    }
}
</script>

<style lang="less">

</style>
