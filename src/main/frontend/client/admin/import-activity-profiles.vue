<template>
    <v-container grid-list-md>
        <v-layout row wrap class="import-activity-profiles-wrapper">

            <section-header>Import Activity Profiles</section-header>

            <v-flex xs12>

                <div>Upload a CSV file with activity profile information to add
                add to the database. The file should contain the following
                columns (data can be sparse):</div>

                <ul class="column-listing">
                    <li>AAA</li>
                    <li>BBB</li>
                    <li>CCC</li>
                </ul>

                <!--<file-dropzone></file-dropzone>-->
            </v-flex>
        </v-layout>
    </v-container>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import SectionHeader from '../header.vue';
import FileDropzone from '../file-dropzone.vue';

@Component({ components: { SectionHeader, FileDropzone } })
export default class ImportActivityProfiles extends Vue {

}
</script>

<style lang="less">
.import-activity-profiles-wrapper {

    .column-listing {
        padding: 1rem 0;
    }
}
</style>
