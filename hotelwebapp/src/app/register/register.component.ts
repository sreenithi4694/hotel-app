import {Component, OnInit} from '@angular/core';
import {FormGroup, FormControl, Validators, FormBuilder} from '@angular/forms'
import {AuthService} from "../service/auth.service";
import {MatSnackBar} from '@angular/material/snack-bar';
import {AlertDialogComponent} from "../alert-dialog/alert-dialog.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent implements OnInit{
  public registerForm!: FormGroup;
  hide = true;


  constructor(private authService:AuthService,private snackBack:MatSnackBar
  ,private dialog : MatDialog,private formBuilder:FormBuilder) {

  }

  onSubmit() {

    console.log(this.registerForm.value);
    this.authService.register(this.registerForm.value.name,this.registerForm.value.email,this.registerForm.value.password).subscribe(
      {
        next: data => {
            console.log(data);
          this.dialog.open(AlertDialogComponent,{
            data:{
              message: 'User Register Successfully Kindly click on login to Proceed',
              buttonText: 'Login',
              linkValue : 'login'
            },
          });
        },
        error: err => {
            console.log(err);
          // this.snackBack.open(err.error.message , "OK",{
          //   duration :3000
          // });

          this.dialog.open(AlertDialogComponent,{
            data:{
              message: err.error.message,
              buttonText: 'OK',
              //linkValue : 'register'
            },
          });
        }
      }
    )
  }

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      name : ['',[Validators.required, Validators.minLength(6), Validators.maxLength(40)]],
      email : ['',[Validators.required, Validators.email, Validators.max(50)]],
      password : ['',[Validators.required, Validators.minLength(6), Validators.maxLength(40)]]
      //name: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(40)]),
      //email: new FormControl('', [Validators.required, Validators.email, Validators.max(50)]),
      //password: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(40)]),
    });


  }


}
