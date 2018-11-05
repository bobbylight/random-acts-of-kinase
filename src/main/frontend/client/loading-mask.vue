<template functional>
    <div class="loading-mask">
        <v-progress-circular
            :size="50"
            color="amber"
            indeterminate
        ></v-progress-circular>
    </div>
</template>

<style type="less">
.loading-mask {
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>
