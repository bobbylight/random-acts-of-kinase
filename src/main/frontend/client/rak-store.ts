import Vuex, { Store } from 'vuex';
import { RakState } from './rak';
import Vue from 'vue';

Vue.use(Vuex);

const store: Store<RakState> = new Store({
    state: {
        user: ''
    },
    mutations: {
        setUser(state: any, user: string) {
            state.user = user;
        }
    },
    getters: {
        loggedIn(state: RakState): boolean {
            return !!state.user;
        }
    }
});

export default store;
