import {AfterViewInit, Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, NgForm, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "../service/auth.service";
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatDialog} from "@angular/material/dialog";
import {AlertDialogComponent} from "../alert-dialog/alert-dialog.component";

declare var google : any;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit , AfterViewInit{
  hide: Boolean | undefined= true;
  username :any;
  password:any;
  public loginform!: FormGroup;


  constructor(private router:Router
    ,private authService:AuthService,private snackBack:MatSnackBar,
              private dialog :MatDialog,private formBuilder : FormBuilder) {

  }


  ngOnInit(): void {
    // this.loginform = new FormGroup({
    //   username: new FormControl('', [Validators.required, Validators.email]),
    //   password: new FormControl('', Validators.required),
    // });

    this.loginform = this.formBuilder.group({
      username : ['',[Validators.required,Validators.email]],
      password : ['',[Validators.required]]
    })
  }

  onSubmit(){
    //console.log(form);
    this.authService.signIn(this.loginform.value.username,this.loginform.value.password).subscribe({
      next :data=>{
        console.log(data);
        let token=data.token;
        token = token.substring(token.indexOf("bezkoder="),token.indexOf("; Path")).replace("bezkoder=","");
        const userData = this.decodeJWTToken(token);
        sessionStorage.setItem("token",token);
        sessionStorage.setItem("loggedInUser",JSON.stringify(userData));
        sessionStorage.setItem("isGoogleLogin","FALSE");
        console.log(token);
        this.router.navigate(['homePage'])
      },
      error:err=>{
        console.log(err);
        if(err.status == 401){
          // this.snackBack.open("User is not authorized Kindly check the credentails" , "OK",{
          //   duration :3000
          // });
          this.dialog.open(AlertDialogComponent,{
            data:{
              message: 'User is not authorized Kindly check the Credentails',
              buttonText: 'OK',
              //linkValue : 'login'
            },
          });
        } else if (err.error.message){
          this.dialog.open(AlertDialogComponent,{
            data:{
              message:err.error.message,
              buttonText: 'OK',
              //linkValue : ''
            },
          });
        } else {
          this.dialog.open(AlertDialogComponent,{
            data:{
              message: 'Unexpected error occurred , Kindly try after sometime',
              buttonText: 'OK',
              //linkValue : ''
            },
          });
        }

        //this.dialog.open()
      }
    });


  }

  decodeJWTToken(token:string){
    return JSON.parse(atob(token.split(".")[1]));
  }

  handleLoginResponse(resp:any){
    console.log("in handleLoginResponse ");
    console.log(resp)
    const userData = this.decodeJWTToken(resp.credential);
    sessionStorage.setItem("token",resp.credential);
    sessionStorage.setItem("loggedInUser",JSON.stringify(userData));
    sessionStorage.setItem("isGoogleLogin","YES");
    this.authService.handleGoogleSignin(userData.name,userData.email).subscribe(
      data =>{
       console.log(data);
        this.router.navigate(['homePage']);
      },
      error => {
        console.log(error);
      }
    )
    //this.router.navigate(['homePage'])
  }

  signInButtonClick(){
    console.log("in signInButtonClick ");
  }

  ngAfterViewInit(): void {

    // @ts-ignore
    window.onGoogleLibraryLoad = () => {
      console.log("google loaded");
      // @ts-ignore
      }

      // @ts-ignore
      google.accounts.id.initialize({
          client_id: '414016060073-sqrupk6tbifp170f9122bfljiiuc63fd.apps.googleusercontent.com',
          callback: (resp: any) => this.handleLoginResponse(resp)
        }
      );

      google.accounts.id.renderButton(document.getElementById('google-btn'),
        {
          type: 'standard',
          theme: 'filled_blue',
          size: 'medium',
          //text: 'continue_with',
          width: '200',
          click_listener: this.signInButtonClick
        });


  }

}
