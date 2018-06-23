import axios, { AxiosError, AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';
import {
    ActivityProfile,
    Audit,
    BlogPost,
    Compound,
    ErrorResponse,
    Feedback,
    ObjectImportRep,
    PagedDataRep,
    Partner,
    SearchFilter,
    UserRep
} from './rak';
import rakUtil from './util';

export class RestApi {

    private readonly instance: AxiosInstance;

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

    createFeedback(feedback: Feedback): Promise<Feedback> {

        return this.instance.post('api/feedback', feedback)
            .then((response: AxiosResponse<Feedback>) => {
                return response.data;
            })
            .catch((error: AxiosError) => {
                throw RestApi.axiosErrorToErrorResponse(error);
            });
    }

    deleteFeedback(feedbackId: number): Promise<any> {

        return this.instance.delete(`api/feedback/${feedbackId}`)
            .then((response: AxiosResponse<void>) => {
                return undefined;
            })
            .catch((error: AxiosError) => {
                throw RestApi.axiosErrorToErrorResponse(error);
            });
    }

    downloadCompoundImage(compoundName: string, width: number | undefined,
                          height: number | undefined) {

        let url: string = `api/compounds/images/${compoundName}?download=true`;
        if (width && height) {
            url += `&width=${width}&height=${height}`;
        }

        rakUtil.programmaticallyClickLink(url);
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

    getAuditRecords(page: number, size: number, filters: any,
                 sortParam: string): Promise<PagedDataRep<Audit>> {

        let url: string = `admin/api/audits?page=${page}&size=${size}`;
        if (filters.userName) {
            url += `&userName=${filters.userName}`;
        }

        if (sortParam) {
            url += `&sort=${sortParam}`;
        }

        return this.instance.get(url)
            .then((response: AxiosResponse<PagedDataRep<Audit>>) => {
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

    getCompound(compoundName: string): Promise<Compound> {

        return this.instance.get(`api/compounds/${compoundName}`)
            .then((response: AxiosResponse<Compound>) => {
                return response.data;
            })
            .catch((error: AxiosError) => {
                throw RestApi.axiosErrorToErrorResponse(error);
            });
    }

    getCompounds(page: number, size: number, filters: SearchFilter): Promise<PagedDataRep<Compound>> {

        let url: string = `api/compounds?page=${page}&size=${size}`;
        if (filters.inhibitor) {
            url += `&compound=${filters.inhibitor}`;
        }
        if (filters.activityOrKd === 'percentControl' && filters.activity) {
            url += `&activity=${filters.activity}`;
        }
        else if (filters.activityOrKd === 'kd' && filters.kd) {
            url += `&kd=${filters.kd}`;
        }
        if (filters.kinase) {
            url += `&kinase=${filters.kinase}`;
        }

        return this.instance.get(url)
            .then((response: AxiosResponse<PagedDataRep<Compound>>) => {
                return response.data;
            });
    }

    getFeedback(page: number, size: number, filters: any,
                 sortParam: string): Promise<PagedDataRep<Feedback>> {

        let url: string = `api/feedback?page=${page}&size=${size}`;
        if (filters.author) {
            url += `&author=${filters.author}`;
        }

        if (sortParam) {
            url += `&sort=${sortParam}`;
        }

        return this.instance.get(url)
            .then((response: AxiosResponse<PagedDataRep<Feedback>>) => {
                return response.data;
            });
    }

    getPartners(): Promise<PagedDataRep<Partner>> {
        return this.instance.get('api/partners?page=0&size=1000')
            .then((response: AxiosResponse<PagedDataRep<Partner>>) => {
                return response.data;
            });
    }

    importActivityProfiles(file: File, headerRow: boolean, commit: boolean = true): Promise<ObjectImportRep> {
        return this.importCsvDataImpl('admin/api/activityProfiles', file, headerRow, commit);
    }

    importCompounds(file: File, headerRow: boolean, commit: boolean = true): Promise<ObjectImportRep> {
        return this.importCsvDataImpl('admin/api/compounds', file, headerRow, commit);
    }

    private importCsvDataImpl(baseUrl: string, file: File, headerRow: boolean,
                              commit: boolean = true): Promise<ObjectImportRep> {

        const data: FormData = new FormData();
        data.append('file', file);

        const url: string = `${baseUrl}?commit=${commit}&headerRow=${headerRow}`;

        return this.instance.patch(url, data)
            .then((response: AxiosResponse<ObjectImportRep>) => {
                return response.data;
            })
            .catch((error: AxiosError) => {
                throw RestApi.axiosErrorToErrorResponse(error);
            });
    }

    importKds(file: File, headerRow: boolean, commit: boolean = true): Promise<ObjectImportRep> {
        return this.importCsvDataImpl('admin/api/kdValues', file, headerRow, commit);
    }

    importS10s(file: File, headerRow: boolean, commit: boolean = true): Promise<ObjectImportRep> {
        return this.importCsvDataImpl('admin/api/sScores', file, headerRow, commit);
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
