import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';

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

    login(user: string, password: string): Promise<any> {

        const config: AxiosRequestConfig = {
            auth: {
                username: user,
                password
            }
        };

        return this.instance.post('login', null, config)
            .then((response: AxiosResponse<any>) => {
                console.log('All done!  Response: ' + JSON.stringify(response));
                return response.data;
            });
    }
}

export default new RestApi();
