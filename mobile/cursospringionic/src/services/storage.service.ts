import { Injectable } from "@angular/core";
import { LocalUser } from "../models/local_user";
import { STORAGE_KEYS } from "../config/storage_keys.config";

@Injectable()
export class StorageService {

    getLocalUser() : LocalUser {
        let usr = localStorage.getItem(STORAGE_KEYS.localUser);
        if(usr == null) {
            return null;
        }
        else {
            //Converte o objeto string que está vindo do LocalStorage para JSON
            return JSON.parse(usr);
        }
    }

    setLocalUser(obj : LocalUser) {
        if( obj == null) {
            //Se objeto vier nulo, removemos o objeto chave/valor do LocalStorage
            localStorage.removeItem(STORAGE_KEYS.localUser);
        } 
        else {
            //Coloca no LocalStorage convertendo o objeto JSON para uma string (JSON.stringfy)
            localStorage.setItem(STORAGE_KEYS.localUser, JSON.stringify(obj));
        }
    }
}