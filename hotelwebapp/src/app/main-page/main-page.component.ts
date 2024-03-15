import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.scss'
})
export class MainPageComponent implements OnInit{
  ngOnInit(): void {
    //clear the session when user comes to this page
    sessionStorage.clear();
  }


}
