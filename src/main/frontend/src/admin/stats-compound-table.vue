<template>
    <v-flex xs12>

        <v-card class="stats-missing-data-card-padding">

            <actionable-card-title :title="title" :sub-title="subtitle">
                <v-text-field label="Filter by compound" class="stats-filter-field d-inline-block"
                              @input="computeFullUrl"
                              v-model="compoundNameFilter"></v-text-field>
                <download-button :url="fullUrl"
                                 :download-file-name="tweakedDownloadFileName"></download-button>
            </actionable-card-title>

            <v-card-text>
                <compounds-table :url="fullUrl" :columnInfo="tableColumnInfo" :dense="dense"></compounds-table>
            </v-card-text>
        </v-card>
    </v-flex>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import CompoundsTable, { ColumnInfo } from './compounds-table.vue';
import DownloadButton from './download-button.vue';
import SectionHeader from '../header.vue';
import { Prop } from 'vue-property-decorator';
import ActionableCardTitle from '../actionable-card-title.vue';

@Component({ components: { ActionableCardTitle, CompoundsTable, DownloadButton, SectionHeader } })
export default class StatsCompoundTable extends Vue {

    @Prop({ required: true })
    private readonly title: string;

    @Prop({ required: true })
    private readonly subtitle: string;

    @Prop({ required: true })
    private readonly downloadFileName: string;

    @Prop({ required: true })
    private readonly tableUrl: string;

    @Prop({ required: true })
    private readonly tableColumnInfo: ColumnInfo[];

    @Prop({ default: false })
    private readonly dense: boolean;

    compoundNameFilter: string = '';
    private fullUrl: string = '';
    private tweakedDownloadFileName: string = '';

    created() {
        this.computeFullUrl();
    }

    computeFullUrl() {

        let url: string = this.tableUrl;
        this.tweakedDownloadFileName = this.downloadFileName;

        if (this.compoundNameFilter && this.compoundNameFilter.trim().length) {
            url += `?compound=${encodeURIComponent(this.compoundNameFilter.trim())}`;
            this.updateDownloadFileNameForFiltering();
        }
        this.fullUrl = url;
    }

    private updateDownloadFileNameForFiltering() {

        const lastDot: number = this.tweakedDownloadFileName.lastIndexOf('.');
        if (lastDot > -1) {
            this.tweakedDownloadFileName = this.tweakedDownloadFileName.substring(0, lastDot) + '-filtered' +
                this.tweakedDownloadFileName.substring(lastDot);
        }
    }
}
</script>

<style lang="less">
.stats-missing-data-card-padding {
    margin-top: 1rem;
}
</style>
