<template>

    <div>

    <div class="ui top fixed inverted menu">
        <div class="item">
            KINASE!
        </div>
        <a class="item" v-bind:class="{ active: 'search' === activeTab}" v-on:click="setActiveTab('search')">
            Search
        </a>
        <a class="item" v-bind:class="{ active: 'messages' === activeTab}" v-on:click="setActiveTab('messages')">
            Messages
        </a>
        <a class="item" v-bind:class="{ active: 'friends' === activeTab}" v-on:click="setActiveTab('friends')">
            Friends
        </a>
    </div>

    <div class="ui inverted segment">
        <div class="ui inverted secondary menu">
            <a class="active item">
                Home
            </a>
            <a class="item">
                Messages
            </a>
            <a class="item">
                Friends
            </a>
        </div>
    </div>

    </div>

</template>

<script>
    console.log('In the navbar.vue file');
export default {
    name: 'navbar',
    data: () => {
        return {
            activeTab: 'search'
        };
    },
    methods: {
        setActiveTab(tabName) {
            this.activeTab = tabName;
        }
    }
}
</script>

<style scoped>
div {
    color: red;
}
</style>
