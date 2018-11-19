import { API_CONFIG } from './../config/api.config';
import { StorageService } from './../services/storage.service';
import { Injectable } from "@angular/core";
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HTTP_INTERCEPTORS } from "@angular/common/http";
import { Observable } from 'rxjs/Rx';


@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(
        public storageService : StorageService
    ){}

    intercept(req: HttpRequest<any>, next: HttpHandler) : Observable<HttpEvent<any>> {

        let localUser = this.storageService.getLocalUser();
        let N = API_CONFIG.baseUrl.length;
        //Valida e inclui o cabeçalho do Authorization apenas se a requisição for para a nossa aplicação
        let requestToAPI = req.url.substring(0, N) == API_CONFIG.baseUrl;

        if(localUser && requestToAPI) {
            const authReq = req.clone({headers: req.headers.set('Authorization', 'Bearer ' + localUser.token)});
            return next.handle(authReq);
        }
        else {
            return next.handle(req);
        }
    }
}

export const AuthInterceptorProvider = {
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true,
};