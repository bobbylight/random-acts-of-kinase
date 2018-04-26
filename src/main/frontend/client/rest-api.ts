import axios, { AxiosError, AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';
import { ActivityProfile, BlogPost, Compound, CompoundImportRep, ErrorResponse, PagedDataRep, UserRep } from './rak';

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

    /**
     * Grabs the error response from the server, so we don't have to return an Axios-specific construct.
     *
     * @param {AxiosError} error The error received from the server.
     * @return The error response.
     */
    private static axiosErrorToErrorResponse(error: AxiosError): ErrorResponse {

        // AxiosError's data's payload is an ErrorResponse, but it is not a generic type
        // for some reason.  That's fine, we take extra care for non ErrorResponses too.

        if (error.response) {
            if (error.response.data.statusCode && error.response.data.message) {
                return error.response.data;
            }
            return { message: error.message, statusCode: error.response.status };
        }

        console.error(`No response information in error: ${JSON.stringify(error)}`);
        return { message: error.message, statusCode: 0 };
    }

    checkAuthentication(): Promise<UserRep> {
        return this.instance.get('login')
            .then((response: AxiosResponse<UserRep>) => {
                return response.data;
            });
    }

    getActivityProfiles(page: number, size: number, filters: any,
                        sortParam: string): Promise<PagedDataRep<ActivityProfile>> {

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
            .then((response: AxiosResponse<PagedDataRep<ActivityProfile>>) => {
                return response.data;
            });
    }

    getBlogPosts(page: number, size: number, filters: any,
                 sortParam: string): Promise<PagedDataRep<BlogPost>> {

        let url: string = `api/blogPosts?page=${page}&size=${size}`;
        if (filters.author) {
            url += `&author=${filters.author}`;
        }

        if (sortParam) {
            url += `&sort=${sortParam}`;
        }

        return this.instance.get(url)
            .then((response: AxiosResponse<PagedDataRep<BlogPost>>) => {
                return response.data;
            });
    }

    getCompounds(page: number, size: number, filters: any): Promise<PagedDataRep<Compound>> {

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
            .then((response: AxiosResponse<PagedDataRep<Compound>>) => {
                return response.data;
            });
    }

    importCompounds(file: File, commit: boolean = true): Promise<CompoundImportRep> {

        const data: FormData = new FormData();
        data.append('file', file);

        return this.instance.patch(`admin/api/compounds?commit=${commit}`, data)
            .then((response: AxiosResponse<CompoundImportRep>) => {
                return response.data;
            })
            .catch((error: AxiosError) => {
                throw RestApi.axiosErrorToErrorResponse(error);
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
            })
            .catch((error: AxiosError) => {
                throw RestApi.axiosErrorToErrorResponse(error);
            });
    }

    logout(): Promise<any> {
        return this.instance.post('logout')
            .then((response: AxiosResponse<void>) => {
                return null;
            });
    }

    deleteBlogPost(postId: number): Promise<void> {

        const url: string = `api/blogPosts/${postId}`;

        return this.instance.delete(url)
            .then(() => {
                return undefined;
            })
            .catch((error: AxiosError) => {
                throw RestApi.axiosErrorToErrorResponse(error);
            });
    }

    saveBlogPost(post: BlogPost): Promise<BlogPost> {
        return this.instance.post('api/blogPosts', post)
            .then((response: AxiosResponse<BlogPost>) => {
                return response.data;
            })
            .catch((error: AxiosError) => {
                throw RestApi.axiosErrorToErrorResponse(error);
            });
    }

    updateBlogPost(post: BlogPost): Promise<BlogPost> {
        return this.instance.put(`api/blogPosts/${post.id}`, post)
            .then((response: AxiosResponse<BlogPost>) => {
                return response.data;
            })
            .catch((error: AxiosError) => {
                throw RestApi.axiosErrorToErrorResponse(error);
            });
    }
}

export default new RestApi();
