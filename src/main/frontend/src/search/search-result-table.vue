<template>
    <div>
        <v-data-table
            hide-headers
            class="search-result-table"
            :items="items"
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
                            <div class="s10">s(10): {{getDisplayS10(props.item.s10)}}</div>
                            <div class="chemotype" v-if="props.item.chemotype">{{props.item.chemotype}}</div>
                        </div>
                        <div v-if="props.item.hidden" class="hidden-compound-icon">
                            <v-tooltip right>
                                <v-icon slot="activator">fa-mask</v-icon>
                                <span>Only admins can see this compound</span>
                            </v-tooltip>
                        </div>
                    </div>
                </td>
            </template>
        </v-data-table>
    </div>
</template>

<script lang="ts">
import restApi from '../rest-api';
import Blazy from 'blazy';
import { Compound, PagedDataRep } from '@/rak';

export default {
    name: 'search-result-table',
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
            totalItems: 0,
            items: [],
            loading: true,
            pagination: {}
        };
    },
    watch: {
        filters: {
            handler(newValue: any) {
                console.log('Filterable property changed');
                this.reloadTable();
            },
            deep: true
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

        getCompoundImage: function(compoundName: string) {
            return `api/compounds/images/${compoundName}`;
        },

        getCompoundUrl: function(compoundName: string) {
            return `#/compound/${compoundName}`;
        },

        getDisplayS10: function(s10: string | null | undefined) {
            return typeof s10 === 'undefined' ? '?' : s10;
        },

        openCompound: function(compoundName: string) {
            window.location.href = this.getCompoundUrl(compoundName);
        },

        onImageClicked: function(compoundName: string) {
            this.$store.commit('setLightboxImage', this.getCompoundImage(compoundName));
            this.$store.commit('setLightboxTitle', compoundName);
        },

        reloadTable: function() {

            this.loading = true;

            const { sortBy, descending, page, rowsPerPage }: any = this.pagination;

            return restApi.getCompounds(page - 1, rowsPerPage, this.filters)
                .then((pagedData: PagedDataRep<Compound>) => {
                    this.items = pagedData.data;
                    this.totalItems = pagedData.total;
                    this.loading = false;
                    return pagedData;
                });
        },

        resetImages: function() {

            const loadedImages: NodeListOf<any> = this.$el.querySelectorAll('img.b-lazy.b-loaded');

            for (let i: number = 0; i < loadedImages.length; i++) {
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
                container: 'search-result-table'
            });
        }
    }
};
</script>

<style lang="less">
@import '../../styles/app-variables';

.search-result-table {
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
            background-color: #fafafa;//#f8f8f8;

            .compound-name {
                color: #1976D2;//#8080a0;
            }
            .chemotype {
                color: #42A5F5;//#8080a0;
            }
            .s10 {
                color: #42A5F5;//#8080a0;
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
                transform: scale(1.1);
            }
        }
    }

    .hidden-compound-icon {
        height: 100%;
        vertical-align: top;
        display: inline-block;
        margin-top: 0.5rem;

        .v-icon {
            color: rgba(0, 0, 0, 0.2);
            &:hover {
                color: rgba(0, 0, 0, 0.3);
            }
        }
    }
}
</style>
