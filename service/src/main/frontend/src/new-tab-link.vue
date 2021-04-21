<template functional>
    <a :href="props.href" target="_blank" rel="noopener noreferrer">{{props.label || props.href}}</a>
</template>

<script>
export default {
    props: {
        href: String,
        label: String
    }
};
</script>
