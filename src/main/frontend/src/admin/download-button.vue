<template>
    <v-tooltip bottom>
        <v-btn flat icon class="download-button" slot="activator"
               :download="downloadFileName" :href="downloadUrl">
            <v-icon>file_download</v-icon>
        </v-btn>
        <span>Download</span>
    </v-tooltip>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';

@Component
export default class DownloadButton extends Vue {

    @Prop({ required: true })
    private readonly url: string;

    @Prop({ required: true })
    private readonly downloadFileName: string;

    get downloadUrl() {
        const firstParamChar: string = this.url.indexOf('?') > -1 ? '&' : '?';
        return this.url + firstParamChar + 'format=csv&page=0&size=10000';
    }
}
</script>

<style lang="less">
a.download-button {
    margin: 0;
}
</style>
