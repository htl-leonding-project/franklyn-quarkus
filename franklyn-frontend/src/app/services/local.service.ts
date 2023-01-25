import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LocalService {

  constructor() { }

  public saveData(key: string, value: string) {
    localStorage.setItem(key, value);
    //sessionStorage.setItem(key, value)
  }

  public getData(key: string) {
    return localStorage.getItem(key)
    //return sessionStorage.getItem(key)
  }
  public removeData(key: string) {
    localStorage.removeItem(key);
    //sessionStorage.removeItem(key);
  }

  public clearData() {
    //sessionStorage.clear();
    localStorage.clear();
  }
}
