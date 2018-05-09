<template>
    <div>
        <v-data-table
            hide-headers
            class="compound-name-table"
            :items="items"
            :search="search"
            :pagination.sync="pagination"
            :total-items="totalItems"
            :loading="loading"
            :rows-per-page-items='[ 20, 50, 100 ]'
            >

            <template slot="items" slot-scope="props">
                <td>
                    <div class="compound-name-cell">
                        <img class="b-lazy compound-image"
                             src="data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw=="
                             :data-src="getCompoundImage(props.item.compoundName)"
                             @click="onImageClicked(props.item.compoundName)"
                             width=80 height=80>
                        &nbsp;&nbsp;&nbsp;
                        <div class="compound-desc" @click="openCompound(props.item.compoundName)">
                            <span class="compound-name">{{props.item.compoundName}}</span>
                            <br>
                            <div class="s10">s(10): {{props.item.s10 || '?'}}</div>
                            <div class="chemotype" v-if="props.item.chemotype">{{props.item.chemotype}}</div>
                        </div>
                    </div>
                </td>
            </template>
        </v-data-table>
    </div>
</template>

<script>
import restApi from 'rest-api';
import Blazy from 'blazy';

export default {
    name: 'compound-name-table',
    props: {
        //inhibitor: String,
        filters: {
            type: Object,
            required: true
        }
    },
    data: function() {
        return {
            blazy: null,
            search: '',
            totalItems: 0,
            items: [],
            loading: true,
            pagination: {}
        };
    },
    watch: {
        'filters.inhibitor': function(newFilter) {
            console.log('inhibitor changed');
            this.reloadTable();
        },
        'filters.kinase': function(newFilter) {
            console.log('kinase changed');
            this.reloadTable();
        },
        'filters.activity': function(newFilter) {
            console.log('activity changed');
            this.reloadTable();
        },

        pagination: {
            handler () {
                // Note this triggers an unnecessary second query until
                // https://github.com/vuetifyjs/vuetify/issues/3585 is fixed
                this.reloadTable();
            },
            deep: true
        }
    },
    methods: {

        getCompoundImage: function(compoundName) {
            return `api/compounds/images/${compoundName}`;
        },

        getCompoundUrl: function(compoundName) {
            return `#/compound/${compoundName}`;
        },

        openCompound: function(compoundName) {
            window.location.href = this.getCompoundUrl(compoundName);
        },

        onImageClicked: function(compoundName) {
            this.$store.commit('setLightboxImage', this.getCompoundImage(compoundName));
        },

        reloadTable: function() {

            this.loading = true;

            const { sortBy, descending, page, rowsPerPage } = this.pagination;

            return restApi.getCompounds(page - 1, rowsPerPage, this.filters)
                .then(pagedData => {
                    this.items = pagedData.data;
                    this.totalItems = pagedData.total;
                    this.loading = false;
                    return pagedData;
                });
        },

        resetImages: function() {

            const loadedImages = this.$el.querySelectorAll('img.b-lazy.b-loaded');

            for (let i = 0; i < loadedImages.length; i++) {
                loadedImages[i].src = 'data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7';
                loadedImages[i].classList.remove('b-loaded');
            }
        }
    },
    updated: function() {

        if (this.blazy) {
            this.blazy.destroy();
            this.resetImages();
            this.blazy.revalidate();
        }
        else {
            this.blazy = new Blazy({
                container: 'compound-name-table'
            });
        }
    }
};
</script>

<style lang="less">
@import '../styles/app-variables';

.compound-name-table {
    thead {
        display: none;
    }

    tr {

        transition: background-color @transition-time;

        .compound-desc {
            vertical-align: middle;
            display: inline-block;
            cursor: pointer;

            .compound-name {
                text-decoration: none;
                color: gray;
                transition: color @transition-time;
            }
            .s10, .chemotype {
                font-size: medium;
                color: lightgray;
                transition: color @transition-time;
            }
        }

        &:hover {
            background-color: #f8f8f8;

            .compound-name {
                color: #8080a0;
            }
            .chemotype {
                color: #8080a0;
            }
            .s10 {
                color: #8080a0;
            }
        }

        div.compound-name-cell {
            font-size: 1.5rem;
            padding: 0;
        }
    }

    border: none;

    img {
        vertical-align: middle;

        &.compound-image {
            cursor: pointer;
            transition: transform @transition-time;

            &:hover {
                transform: scale(1.2);
            }
        }
    }
}
</style>
