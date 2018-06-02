import Vuex, { Store } from 'vuex';
import { RakState, SearchByKinaseSecondComponent, SearchFilter } from './rak';
import Vue from 'vue';

Vue.use(Vuex);

const store: Store<RakState> = new Store({
    state: {
        user: '',
        lightboxImage: null,
        lightboxTitle: undefined,
        filters: {
            inhibitor: '',
            kinase: '',
            activity: '',
            kd: '',
            activityOrKd: 'percentControl'
        }
    },
    mutations: {
        setLightboxImage(state: any, image: string) {
            state.lightboxImage = image;
        },
        setLightboxTitle(state: any, title: string | undefined) {
            state.lightboxTitle = title;
        },
        setUser(state: any, user: string) {
            state.user = user;
        },
        setFilterByInhibitor(state: any, inhibitor: string) {
            state.filters.inhibitor = inhibitor;
        },
        setFilterByKinase(state: any, kinase: string) {
            state.filters.kinase = kinase;
        },
        setFilterByActivity(state: any, activity: string) {
            state.filters.activity = activity;
        },
        setFilterByKd(state: any, kd: string) {
            state.filters.kd = kd;
        },
        setFilterType(state: any, type: SearchByKinaseSecondComponent) {
            state.filters.activityOrKd = type;
        }
    },
    getters: {
        loggedIn(state: RakState): boolean {
            return !!state.user;
        }
    }
});

export default store;
