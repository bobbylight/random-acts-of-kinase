import axios, { AxiosError, AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';
import { ActivityProfile, BlogPost, Compound, PagedDataRep, UserRep } from './rak';

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

    getActivityProfiles(page: number, size: number, filters: any, sortParam: string): Promise<ActivityProfile[]> {

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
        if (sortParam) {
            url += `&sort=${sortParam}`;
        }

        return this.instance.get(url)
            .then((response: AxiosResponse<ActivityProfile[]>) => {
                return response.data;
            });
    }

    getBlogPosts(page: number, size: number, author: string | null = null): Promise<PagedDataRep<BlogPost[]>> {

        let url: string = `api/blogPosts?page=${page}&size=${size}`;
        if (author) {
            url += `&author=${author}`;
        }

        return this.instance.get(url)
            .then((response: AxiosResponse<PagedDataRep<BlogPost[]>>) => {
                return response.data;
            });
    }

    getCompounds(page: number, size: number, filters: any): Promise<PagedDataRep<Compound[]>> {

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
            .then((response: AxiosResponse<PagedDataRep<Compound[]>>) => {
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
            .then((response: AxiosResponse<void>) => {
                return null;
            });
    }

    saveBlogPost(post: BlogPost): Promise<BlogPost> {
        return this.instance.post('api/blogPosts', post)
            .then((response: AxiosResponse<BlogPost>) => {
                return response.data;
            })
            .catch((error: AxiosError) => {
                // AxiosError's data's payload is an ErrorResponse, but it is not
                // a generic type for some reason
                const message: string = error.response ?
                    error.response.data.message : error.message;
                // Re-throw with a better error message
                throw { name: error.name, message: message, stack: error.stack };
            });
    }
}

export default new RestApi();
