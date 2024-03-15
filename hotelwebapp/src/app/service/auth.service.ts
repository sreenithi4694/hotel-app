import { Injectable } from '@angular/core';
import {Router} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
declare var google :any;
const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

const AUTH_API = 'http://localhost/api/bewebapp/api/auth/';
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private router:Router,private http:HttpClient) { }

  signOut():void{
    //google.accounts.id.disableAutoSelect();
    this.clearSessionStorageValues();
    this.router.navigate(['login']);
  }

  clearSessionStorageValues():void{
    sessionStorage.clear();
  }

  signIn(username:string,password:string):Observable<any>  {
    return this.http.post(AUTH_API + 'signin', {
      'username':username,
      'password':password
    }, httpOptions);
  }

  register(name: string, email: string, password: string): Observable<any> {
    return this.http.post(AUTH_API + 'signup', {
      'name':name,
      'email':email,
      'password':password
    }, httpOptions);
  }

  handleGoogleSignin(name:string,email:string){
    return this.http.post(AUTH_API + 'handleGoogleSign', {
      'name':name,
      'email':email
    }, httpOptions);
  }


}
