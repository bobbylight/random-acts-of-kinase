// WARNING - http://stackoverflow.com/questions/39488660/vue-js-2-0-not-rendering-anything

// load our default (non specific) css
import '../styles/app.less';
import '../node_modules/vuetify/dist/vuetify.min.css';
import '../node_modules/quill/assets/snow.styl';

import { library, dom } from '@fortawesome/fontawesome-svg-core';
import { faCog,
    faComments,
    faDatabase,
    faEdit,
    faExclamation,
    faFilter,
    faHandshake,
    faHistory,
    faMapMarkerAlt,
    faMask,
    faNewspaper,
    faQuestionCircle,
    faTimes,
    faUpload,
    faUser } from '@fortawesome/free-solid-svg-icons';
import { faGithub } from '@fortawesome/free-brands-svg-icons';
library.add(faCog, faComments, faDatabase, faEdit, faExclamation, faFilter, faHandshake, faHistory,
    faMapMarkerAlt, faMask, faNewspaper, faQuestionCircle, faTimes, faUpload, faUser, faGithub);
dom.watch();

// Register the router hooks with their names
// (must be done before registering any components)
import './class-component-hooks';

import Vue from 'vue';
import App from './app.vue';
import VueRouter, { Route } from 'vue-router';
import Toasted from 'vue-toasted';
import VueChartkick from 'vue-chartkick';
import Chart from 'chart.js';

// import Vuetify from 'vuetify';
import Vuetify, {
    VAlert,
    VApp,
    VAutocomplete,
    VAvatar,
    VBtn,
    VCard,
    VCardActions,
    VCardText,
    VCardTitle,
    VCheckbox,
    VChip,
    VContainer,
    VContent,
    VDataIterator,
    VDataTable,
    VDatePicker,
    VDialog,
    VDivider,
    VExpansionPanel,
    VExpansionPanelContent,
    VFabTransition,
    VFlex,
    VFooter,
    VForm,
    VIcon,
    VLayout,
    VList,
    VListGroup,
    VListTileAction,
    VListTileContent,
    VListTile,
    VListTileTitle,
    VMenu,
    VNavigationDrawer,
    VProgressCircular,
    VProgressLinear,
    VSelect,
    VSlider,
    VSpacer,
    VSpeedDial,
    VSwitch,
    VTabs,
    VTextarea,
    VTextField,
    VToolbar,
    VToolbarItems,
    VToolbarTitle,
    VTooltip
    //transitions
} from 'vuetify/lib';

import Home from './home.vue';
import Search from './search/search.vue';
import NanoBretSearch from './nanobret/nanobret-search.vue';
import Blog from './blog.vue';
import NetworkTab from './network-tab.vue';
import Compound from './compound.vue';
import Admin from './admin/admin.vue';
import ImportCompounds from './admin/import-compounds.vue';
import ImportActivityProfiles from './admin/import-activity-profiles.vue';
import ImportKds from './admin/import-kds.vue';
import ImportS10s from './admin/import-s10s.vue';
import Stats from './admin/stats.vue';
import Feedback from './admin/feedback.vue';
import AuditHistory from './admin/audit.vue';
import Partners from './partners.vue';
import BlogManager from './admin/blog-manager.vue';
import { RouteConfig } from 'vue-router/types/router';

Vue.use(VueRouter);
Vue.use(Toasted as any); // cast to any until vue-toasted #82 is fixed
Vue.use(VueChartkick, { adapter: Chart });
Vue.use(Vuetify, {

    components: {
        VAlert,
        VAutocomplete,
        VAvatar,
        VApp,
        VBtn,
        VCard,
        VCardActions,
        VCardText,
        VCardTitle,
        VCheckbox,
        VChip,
        VContainer,
        VContent,
        VDataIterator,
        VDataTable,
        VDatePicker,
        VDialog,
        VDivider,
        VExpansionPanel,
        VExpansionPanelContent,
        VFabTransition,
        VFlex,
        VFooter,
        VForm,
        VIcon,
        VLayout,
        VList,
        VListGroup,
        VListTileAction,
        VListTileContent,
        VListTile,
        VListTileTitle,
        VMenu,
        VNavigationDrawer,
        VProgressCircular,
        VProgressLinear,
        VSelect,
        VSlider,
        VSpacer,
        VSpeedDial,
        VSwitch,
        VTabs,
        VTextarea,
        VTextField,
        VToolbar,
        VToolbarItems,
        VToolbarTitle,
        VTooltip
        //transitions
    }
});

import store from './rak-store';
import ImportNanoBRET from './admin/import-nanobret.vue';

window.onload = () => {

    const routes: RouteConfig[] = [
        {
            path: '/',
            name: 'home',
            component: Home
        },
        {
            path: '/search',
            name: 'search',
            component: Search
        },
        {
            path: '/nanoBretSearch',
            name: 'nanoBretSearch',
            component: NanoBretSearch
        },
        {
            path: '/blog',
            name: 'blog',
            component: Blog
        },
        {
            path: '/network',
            name: 'network',
            component: NetworkTab
        },
        {
            path: '/admin',
            component: Admin,
            children: [
                {
                    path: 'import-compounds',
                    name: 'import-compounds',
                    component: ImportCompounds
                },
                {
                    path: 'import-activity-profiles',
                    name: 'import-activity-profiles',
                    component: ImportActivityProfiles
                },
                {
                    path: 'import-kds',
                    name: 'import-kds',
                    component: ImportKds
                },
                {
                    path: 'import-nanobret',
                    name: 'import-nanobret',
                    component: ImportNanoBRET
                },
                {
                    path: 'import-s10s',
                    name: 'import-s10s',
                    component: ImportS10s
                },
                {
                    path: 'blog-manager',
                    name: 'blog-manager',
                    component: BlogManager
                },
                {
                    path: 'stats',
                    name: 'stats',
                    component: Stats
                },
                {
                    path: 'feedback',
                    name: 'feedback',
                    component: Feedback
                },
                {
                    path: 'audit',
                    name: 'audit',
                    component: AuditHistory
                },
                {
                    path: '', // Default view when unknown sub-route specified
                    component: ImportCompounds
                }
            ]
        },
        {
            path: '/compound/:id',
            name: 'compound',
            component: Compound,
            props: true
        },
        {
            path: '/partners',
            name: 'partners',
            component: Partners
        }
    ];

    const router: VueRouter = new VueRouter({
        /*mode: 'history',*/
        routes,
        scrollBehavior: (to: Route, from: Route, savedPosition: any) => {
            return { x: 0, y: 0 };
        }
    });
    router.afterEach((to: Route) => {
        if (to.path.indexOf('/admin') === 0) {
            // Remember the most recent admin sub-route so we go straight back to it when the 'Admin' tab is clicked
            store.commit('setLastAdminRouteName', to.name);
        }
    });

    // tslint:disable-next-line:no-unused-expression
    new Vue({
        el: '#app',
        router,
        store,
        render: (h) => {
            return h(App);
        }
    });
};
