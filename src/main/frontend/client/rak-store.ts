import Vuex, { Store } from 'vuex';
import { RakState, SearchByKinaseSecondComponent } from './rak';
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
        },
        lastAdminRouteName: 'import-compounds'
    },
    mutations: {
        setLightboxImage(state: RakState, image: string) {
            state.lightboxImage = image;
        },
        setLightboxTitle(state: RakState, title: string | undefined) {
            state.lightboxTitle = title;
        },
        setUser(state: RakState, user: string) {
            state.user = user;
        },
        setFilterByInhibitor(state: RakState, inhibitor: string) {
            state.filters.inhibitor = inhibitor;
        },
        setFilterByKinase(state: RakState, kinase: string) {
            state.filters.kinase = kinase;
        },
        setFilterByActivity(state: RakState, activity: string) {
            state.filters.activity = activity;
        },
        setFilterByKd(state: RakState, kd: string) {
            state.filters.kd = kd;
        },
        setFilterType(state: RakState, type: SearchByKinaseSecondComponent) {
            state.filters.activityOrKd = type;
        },
        setLastAdminRouteName(state: RakState, routeName: string) {
            state.lastAdminRouteName = routeName;
        }
    },
    getters: {
        loggedIn(state: RakState): boolean {
            return !!state.user;
        }
    }
});

export default store;
