// WARNING - http://stackoverflow.com/questions/39488660/vue-js-2-0-not-rendering-anything

// load our default (non specific) css
import 'font-awesome/css/font-awesome.css';
// import 'app.less';

import Vue from 'vue';
import App from './App.vue';
import VueRouter from 'vue-router';
import Toasted from 'vue-toasted';
import './../semantic/dist/semantic.css';
import 'datatables.net';
import 'jquery-lazy';
import './../semantic/dist/semantic';
//import 'datatables.net-dt/css/jquery.dataTables.css';
import './dataTables.semanticui';
import Search from './search.vue';
import Compound from './compound.vue';
import Admin from './admin.vue';
import { RouteConfig } from 'vue-router/types/router';

Vue.use(VueRouter);
Vue.use(Toasted);

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
