// WARNING - http://stackoverflow.com/questions/39488660/vue-js-2-0-not-rendering-anything

// load our default (non specific) css
import 'font-awesome/css/font-awesome.css';
import 'app.less';
import '../node_modules/vuetify/dist/vuetify.min.css';

import Vue from 'vue';
import App from './App.vue';
import VueRouter from 'vue-router';
import Toasted from 'vue-toasted';

// import Vuetify from 'vuetify';
import Vuetify from 'vuetify/es5/components/Vuetify';
import VAlert from 'vuetify/es5/components/VAlert';
import VApp from 'vuetify/es5/components/VApp';
import VBtn from 'vuetify/es5/components/VBtn';
import VCard from 'vuetify/es5/components/VCard';
import VDataTable from 'vuetify/es5/components/VDataTable';
import VDialog from 'vuetify/es5/components/VDialog';
import VFooter from 'vuetify/es5/components/VFooter';
import VGrid from 'vuetify/es5/components/VGrid'; // VContainer, VContent, VFlex, VGrid, VLayout, VSpacer
import VIcon from 'vuetify/es5/components/VIcon';
import VProgressCircular from 'vuetify/es5/components/VProgressCircular';
import VProgressLinear from 'vuetify/es5/components/VProgressLinear';
import VSelect from 'vuetify/es5/components/VSelect';
import VTextField from 'vuetify/es5/components/VTextField';
import VToolbar from 'vuetify/es5/components/VToolbar';
import transitions from 'vuetify/es5/components/transitions';

//import './../semantic/dist/semantic.css';
import 'datatables.net';
import 'jquery-lazy';
//import './../semantic/dist/semantic';
//import 'datatables.net-dt/css/jquery.dataTables.css';
import './dataTables.semanticui';
import Search from './search.vue';
import Compound from './compound.vue';
import Admin from './admin/admin.vue';
import { RouteConfig } from 'vue-router/types/router';

Vue.use(VueRouter);
Vue.use(Toasted);
// Vue.use(Vuetify);
Vue.use(Vuetify, {
    components: {
        VAlert,
        VApp,
        VBtn,
        VCard,
        VDataTable,
        VDialog,
        VFooter,
        VGrid,
        VIcon,
        VProgressCircular,
        VProgressLinear,
        VSelect,
        VTextField,
        VToolbar,
        transitions
    }
});

import store from './rak-store';

window.onload = () => {

    const routes: RouteConfig[] = [
        {
            path: '/',
            name: 'home',
            component: Search
        },
        {
            path: '/admin',
            name: 'admin',
            component: Admin
        },
        {
            path: '/compound/:id',
            name: 'compound',
            component: Compound,
            props: true
        }
    ];

    const router: VueRouter = new VueRouter({
        /*mode: 'history'w,*/
        routes
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
