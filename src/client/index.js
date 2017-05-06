// WARNING - http://stackoverflow.com/questions/39488660/vue-js-2-0-not-rendering-anything

// load our default (non specific) css
// import 'font-awesome/css/font-awesome.css';
// //import 'bootstrap/dist/css/bootstrap.css';
// //import 'bootstrap-vue/dist/bootstrap-vue.css';
// import 'app.less';

import Vue from 'vue';
import App from './App.vue';
import './../../semantic/dist/semantic.min.css';
import 'datatables.net';
//import 'datatables.net-dt/css/jquery.dataTables.css';
import './dataTables.semanticui';

window.onload = () => {
    new Vue({
        el: '#app',
        render: (h) => {
            console.log('Runnieng h(App)... ' + App + ', ' + document.getElementById('app'));
            return h(App);
        },
        created: function() {
            console.log('index.js created');
        },
        mounted: function() {
            console.log('index.js mounted');
        },
        destroyed: function() {
            console.log('index.js destroyed');
        }
    });
};
