<template>
    <select :name="name" class="ui fluid search selection dropdown">
    </select>
</template>

<script>

function responseToSemanticStructure(origResponse) {

    const response = {
        success: true,
        results: origResponse.data.map(item => {
            return {
                name: item.discoverxGeneSymbol,
                value: item.discoverxGeneSymbol
            };
        })
    };

    return response;
}

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
                cache: false,
                onResponse: responseToSemanticStructure
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
