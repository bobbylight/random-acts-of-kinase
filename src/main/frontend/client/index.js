// WARNING - http://stackoverflow.com/questions/39488660/vue-js-2-0-not-rendering-anything

// load our default (non specific) css
import 'font-awesome/css/font-awesome.css';
// //import 'bootstrap/dist/css/bootstrap.css';
// //import 'bootstrap-vue/dist/bootstrap-vue.css';
// import 'app.less';

import Vue from 'vue';
import App from './App.vue';
import VueRouter from 'vue-router';
import './../semantic/dist/semantic.min.css';
import 'datatables.net';
import 'jquery-lazy';
import './../semantic/dist/semantic';
//import 'datatables.net-dt/css/jquery.dataTables.css';
import './dataTables.semanticui';
import Search from './search.vue';
import Compound from './compound.vue';

Vue.use(VueRouter);

window.onload = () => {

    const routes = [
        {
            path: '/',
            name: 'home',
            component: Search
        },
        {
            path: '/compound/:id',
            name: 'compound',
            component: Compound,
            props: true
        }
    ];

    const router = new VueRouter({
        /*mode: 'history'w,*/
        routes
    });

    new Vue({
        el: '#app',
        router: router,
        render: (h) => {
            return h(App);
        }
    });
};
