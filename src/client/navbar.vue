<template>
    <div class="ui top fixed inverted menu">
        <div class="item">
            KINASE!
        </div>
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
</template>

<script>
    console.log('In the navbar.vue file');
export default {
    name: 'navbar',
    data: () => {
        return {

        };
    }
}
</script>

<style scoped>
div {
    color: red;
}
</style>
