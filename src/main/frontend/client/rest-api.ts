import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';
import { ActivityProfile, Compound, UserRep } from './rak';

export class RestApi {

    private instance: AxiosInstance;

    constructor() {
        this.instance = axios.create({
            headers: {
                // Stops Spring Boot from challenging authenticated URLs with
                // "WWW-Authenticate: Basic header" (which triggers basic auth modal)
                'X-Requested-With': 'XMLHttpRequest'
            }
        });
    }

    checkAuthentication(): Promise<UserRep> {
        return this.instance.get('login')
            .then((response: AxiosResponse<UserRep>) => {
                return response.data;
            });
    }

    getActivityProfiles(page: number, size: number, filters: any): Promise<ActivityProfile[]> {

        let url: string = `api/activityProfiles?page=${page}&size=${size}`;
        if (filters.inhibitor) {
            url += `&compound=${filters.inhibitor}`;
        }
        if (filters.activity) {
            url += `&activity=${filters.activity}`;
        }
        if (filters.kinase) {
            url += `&kinase=${filters.kinase}`;
        }

        return this.instance.get(url)
            .then((response: AxiosResponse<ActivityProfile[]>) => {
                return response.data;
            });
    }

    getCompounds(page: number, size: number, filters: any): Promise<Compound[]> {

        let url: string = `api/compounds?page=${page}&size=${size}`;
        if (filters.inhibitor) {
            url += `&compound=${filters.inhibitor}`;
        }
        if (filters.activity) {
            url += `&activity=${filters.activity}`;
        }
        if (filters.kinase) {
            url += `&kinase=${filters.kinase}`;
        }

        return this.instance.get(url)
            .then((response: AxiosResponse<Compound[]>) => {
                return response.data;
            });
    }

    login(user: string, password: string): Promise<UserRep> {

        const config: AxiosRequestConfig = {
            auth: {
                username: user,
                password
            }
        };

        return this.instance.get('login', config)
            .then((response: AxiosResponse<UserRep>) => {
                return response.data;
            });
    }

    logout(): Promise<any> {
        return this.instance.post('logout')
            .then((response: AxiosResponse) => {
                return null;
            });
    }
}

export default new RestApi();
