<template>
    <v-flex xs12>

        <v-card class="stats-missing-data-card-padding">

            <v-card-title primary-title>
                <div class="title-content">
                    <div>
                        <h3 class="headline">{{title}}</h3>
                        <div>{{subtitle}}</div>
                    </div>

                    <div class="admin-card-button-area">
                        <v-text-field label="Filter by compound" class="stats-filter-field d-inline-block"
                                      @input="computeFullUrl"
                                      v-model="compoundNameFilter"></v-text-field>
                        <download-button :url="fullUrl"
                                         :download-file-name="tweakedDownloadFileName"></download-button>
                    </div>
                </div>
            </v-card-title>

            <v-card-text>
                <compounds-table :url="fullUrl" :columnInfo="tableColumnInfo"></compounds-table>
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

@Component({ components: { CompoundsTable, DownloadButton, SectionHeader } })
export default class StatsCompoundTable extends Vue {

    @Prop({ required: true })
    private title: string;

    @Prop({ required: true })
    private subtitle: string;

    @Prop({ required: true })
    private downloadFileName: string;

    @Prop({ required: true })
    private tableUrl: string;

    @Prop({ required: true })
    private tableColumnInfo: ColumnInfo[];

    private compoundNameFilter: string = '';
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
.title-content {
    position: relative;
    width: 100%;

    .admin-card-button-area {
        position: absolute;
        top: 0;
        right: 0;

        .stats-filter-field {
            margin-right: 1rem;
        }
    }
}
</style>
