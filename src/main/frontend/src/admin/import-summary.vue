<template>
    <div>

        <div v-if="statusMessage">
            {{statusMessage}}
        </div>

        <div v-if="!statusMessage">
            Creating {{newCount}} records, modifying {{modifiedCount}} ({{totalCount}} total)
        </div>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import { Prop } from 'vue-property-decorator';

export type LoadingStatus = 'loading' | 'loaded' | 'error';

export type PreviewGridFilterType = 'none' | 'new' | 'modified' | 'unmodified';


@Component
export default class ImportSummary extends Vue {

    @Prop({ required: true })
    private readonly newCount: number;

    @Prop({ required: true })
    private readonly modifiedCount: number;

    @Prop({ required: true })
    private readonly totalCount: number;

    @Prop({ required: true })
    private readonly status: LoadingStatus;

    get statusMessage(): string | null {

        switch (this.status) {
            case 'error':
                return 'An error occurred loading the data.';
            case 'loading':
                return 'Loading, please wait...';
        }

        return null;
    }
}
</script>
