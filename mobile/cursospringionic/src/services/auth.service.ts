import { API_CONFIG } from './../config/api.config';
import { HttpClient } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { CredenciaisDTO } from "../models/credenciais.dto";
import { LocalUser } from '../models/local_user';
import { StorageService } from './storage.service';
import { JwtHelper} from 'angular2-jwt';

@Injectable()
export class AuthService {

    jwtHelper: JwtHelper = new JwtHelper();

    constructor(
        public http: HttpClient,
        public storage: StorageService
    ){

    }

    authenticate(credenciais: CredenciaisDTO) {
        return this.http.post(`${API_CONFIG.baseUrl}/login`,
        credenciais,
        {
            observe: 'response',
            responseType: 'text'
        })
    }

    successfulLogin(authrizationValue : string) {
        //É necessário fazer o substring para retirar o BEARER do token, pois vem como padrão.
        let token = authrizationValue.substring(7);
        let user : LocalUser = {
            token: token,
            // Recupera o email a partir do token
            email: this.jwtHelper.decodeToken(token).sub,
        };

        this.storage.setLocalUser(user);
    }

    logout() {
        this.storage.setLocalUser(null);
    }
}