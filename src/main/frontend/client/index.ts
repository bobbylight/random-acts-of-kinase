// WARNING - http://stackoverflow.com/questions/39488660/vue-js-2-0-not-rendering-anything

// load our default (non specific) css
import 'app.less';
import '../node_modules/vuetify/dist/vuetify.min.css';
import '../node_modules/quill/assets/snow.styl';

import fontawesome from '@fortawesome/fontawesome';
import faCog from '@fortawesome/fontawesome-free-solid/faCog';
import faComments from '@fortawesome/fontawesome-free-solid/faComments';
import faDatabase from '@fortawesome/fontawesome-free-solid/faDatabase';
import faFilter from '@fortawesome/fontawesome-free-solid/faFilter';
import faHandshake from '@fortawesome/fontawesome-free-solid/faHandshake';
import faHistory from '@fortawesome/fontawesome-free-solid/faHistory';
import faNewspaper from '@fortawesome/fontawesome-free-solid/faNewspaper';
import faQuestionCircle from '@fortawesome/fontawesome-free-solid/faQuestionCircle';
import faTimes from '@fortawesome/fontawesome-free-solid/faTimes';
import faUpload from '@fortawesome/fontawesome-free-solid/faUpload';
import faUser from '@fortawesome/fontawesome-free-solid/faUser';
import faGithub from '@fortawesome/fontawesome-free-brands/faGithub';
fontawesome.library.add(faCog, faComments, faDatabase, faFilter, faHandshake, faHistory, faNewspaper,
    faQuestionCircle, faTimes, faUpload, faUser, faGithub);

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
import Vuetify from 'vuetify/es5/components/Vuetify';
import VAlert from 'vuetify/es5/components/VAlert';
import VApp from 'vuetify/es5/components/VApp';
import VAvatar from 'vuetify/es5/components/VAvatar';
import VBtn from 'vuetify/es5/components/VBtn';
import VCard from 'vuetify/es5/components/VCard';
import VCheckbox from 'vuetify/es5/components/VCheckbox';
import VChip from 'vuetify/es5/components/VChip';
import VDataIterator from 'vuetify/es5/components/VDataIterator';
import VDataTable from 'vuetify/es5/components/VDataTable';
import VDialog from 'vuetify/es5/components/VDialog';
import VDivider from 'vuetify/es5/components/VDivider';
import VExpansionPanel from 'vuetify/es5/components/VExpansionPanel';
import VFooter from 'vuetify/es5/components/VFooter';
import VGrid from 'vuetify/es5/components/VGrid'; // VContainer, VContent, VFlex, VGrid, VLayout, VSpacer
import VIcon from 'vuetify/es5/components/VIcon';
import VList from 'vuetify/es5/components/VList';
import VMenu from 'vuetify/es5/components/VMenu';
import VNavigationDrawer from 'vuetify/es5/components/VNavigationDrawer';
import VProgressCircular from 'vuetify/es5/components/VProgressCircular';
import VProgressLinear from 'vuetify/es5/components/VProgressLinear';
import VSelect from 'vuetify/es5/components/VSelect';
import VTabs from 'vuetify/es5/components/VTabs';
import VTextField from 'vuetify/es5/components/VTextField';
import VToolbar from 'vuetify/es5/components/VToolbar';
import VTooltip from 'vuetify/es5/components/VTooltip';
import transitions from 'vuetify/es5/components/transitions';

import Home from './home.vue';
import Search from './search/search.vue';
import Blog from './blog.vue';
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
Vue.use(Toasted);
Vue.use(VueChartkick, { adapter: Chart });
// Vue.use(Vuetify);
Vue.use(Vuetify, {
    components: {
        VAlert,
        VAvatar,
        VApp,
        VBtn,
        VCard,
        VCheckbox,
        VChip,
        VDataIterator,
        VDataTable,
        VDialog,
        VDivider,
        VExpansionPanel,
        VFooter,
        VGrid,
        VIcon,
        VList,
        VMenu,
        VNavigationDrawer,
        VProgressCircular,
        VProgressLinear,
        VSelect,
        VTabs,
        VTextField,
        VToolbar,
        VTooltip,
        transitions
    }
});

import store from './rak-store';

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
            path: '/blog',
            name: 'blog',
            component: Blog
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
