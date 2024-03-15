import {AfterViewInit, Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../service/auth.service";
import {Observable} from "rxjs";
import {UserdataService} from "../service/userdata.service";

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.scss'
})
export class HomepageComponent implements OnInit,AfterViewInit{

  userDetails : string | undefined;
  loggedInUserName : string  | undefined;
  constructor(private router:Router,private authService:AuthService,private userDataService:UserdataService) {
  }

  signOut():void{
    this.authService.signOut();
  }

  ngAfterViewInit(): void {
  }

  ngOnInit(): void {
      // @ts-ignore
      this.userDetails = JSON.parse(sessionStorage.getItem('loggedInUser'));
      // @ts-ignore
      this.loggedInUserName = this.userDetails.name;

  }

  getOrderDetails(id:any):void{
    this.userDataService.getOrderData(id).subscribe(data=>{
      console.log(data);
    })
  }

}
