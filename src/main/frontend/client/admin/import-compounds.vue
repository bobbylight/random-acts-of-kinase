<template>
    <v-container grid-list-md>
        <v-layout row wrap class="import-compounds-wrapper">

            <section-header>Import Compounds</section-header>

            <v-flex xs12>

                <!--<h3 class="headline">Incomplete Compounds</h3>-->
                <div>Upload a CSV file with compound information to add it
                to the database. The file should contain the following columns
                (data can be sparse):</div>

                <ul class="column-listing">
                    <li>Compound Name</li>
                    <li>Chemotype</li>
                    <li>s(10)</li>
                    <li>SMILES String</li>
                    <li>Source ("hidden" to hide in the UI)</li>
                </ul>

                <file-dropzone></file-dropzone>
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
export default class ImportCompounds extends Vue {

}
</script>

<style lang="less">
.import-compounds-wrapper {

    .column-listing {
        padding: 1rem 0;
    }
}
</style>
