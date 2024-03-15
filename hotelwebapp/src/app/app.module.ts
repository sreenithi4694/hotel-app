import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {AuthGuardService} from "./service/auth-guard.service";
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HomepageComponent } from './homepage/homepage.component';
import {AuthService} from "./service/auth.service";
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import {authInterceptorProviders} from "./_helpers/auth-interceptor";
import { HttpClientModule } from '@angular/common/http';
import {MatDialogModule} from '@angular/material/dialog';
import {MatCardModule} from "@angular/material/card";
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import { AlertDialogComponent } from './alert-dialog/alert-dialog.component';
import { MainPageComponent } from './main-page/main-page.component';
import { APP_BASE_HREF } from '@angular/common';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomepageComponent,
    LoginComponent,
    RegisterComponent,
    AlertDialogComponent,
    MainPageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    FormsModule,
    MatFormFieldModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatDialogModule,
    ReactiveFormsModule,
    MatCardModule,
    MatSnackBarModule,
    MatProgressSpinner
  ],
  //entryComponents :[AlertDialogComponent],
  providers: [AuthService,AuthGuardService,authInterceptorProviders,
    {provide: APP_BASE_HREF, useValue: '/fewebapp'}
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }
