<template>
    <div class="ui container admin-main">

        <div class="no-access" v-if="!this.$store.getters.loggedIn">

            <h1>You don't have access to this information :(</h1>

            <router-link :to="{ name: 'home' }">Get out of here</router-link>
        </div>

        <div v-if="this.$store.getters.loggedIn">

            <div class="ui cards">

                <div class="card rak-admin-card">

                    <div class="content">
                        <div class="header">Incomplete Compounds</div>
                        <div class="meta">Compounds without a SMILES string or s(10)</div>
                        <div class="description">
                            <IncompleteCompoundsTable></IncompleteCompoundsTable>
                        </div>
                    </div>

                    <div class="card-action-buttons">
                        <a class="ui blue icon button" aria-label="Download"
                                href="/admin/api/incompleteCompounds.csv?page=0&size=10000"
                                data-tooltip="Download" data-inverted data-position="right center">
                            <i class="download icon"></i>
                        </a>
                    </div>
                </div>

            </div>

            <div class="ui cards">

                <div class="card rak-admin-card">

                    <div class="content">
                        <div class="header">Compounds Missing Activity Profiles</div>
                        <div class="meta">Compounds missing some activity profiles</div>
                        <div class="description">
                            Nothing here yet.
                        </div>
                    </div>

                    <div class="card-action-buttons">
                        <a class="ui blue icon button" aria-label="Download"
                           href="/admin/api/compoundsMissingActivityProfiles.csv?page=0&size=10000"
                           data-tooltip="Download" data-inverted data-position="right center">
                            <i class="download icon"></i>
                        </a>
                    </div>
                </div>

            </div>

            <div class="ui cards">

                <div class="card rak-admin-card">
                    <div class="content">
                        <div class="header">Something Else</div>
                        <div class="meta">Some other administrative stuff</div>
                        <div class="description">
                            Nothing here yet.
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</template>

<script lang="ts">
import Vue from 'vue';
import Component from 'vue-class-component';
import IncompleteCompoundsTable from './incomplete-compounds-table.vue';

@Component({ components: { IncompleteCompoundsTable } })
export default class AdminHome extends Vue {
}
</script>

<style lang="less">
.admin-main {

    padding-top: 1rem; // Matches search.vue

    // Our cards are nicely sized and centered
    .ui.cards > .card.rak-admin-card {

        width: 800px;
        margin-left: auto;
        margin-right: auto;

        .card-action-buttons {

            position: absolute;
            top: 1rem;
            right: 1rem;

            button {
                margin-right: 0; // Override semantic ui's .25em margin
            }
        }
    }
}
</style>
