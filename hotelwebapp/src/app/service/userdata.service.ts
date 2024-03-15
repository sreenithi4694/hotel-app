import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";

const DATA_API = 'http://localhost:8080/getOrderData/';
const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};
@Injectable({
  providedIn: 'root'
})
export class UserdataService {

  constructor(private http:HttpClient) { }

  getOrderData(userId:any):Observable<any>{
    return this.http.get(DATA_API+userId, httpOptions);
  }
}
