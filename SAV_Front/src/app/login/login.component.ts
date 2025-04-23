import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth-service.service';
import { JwtService } from '../services/jwt-services.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule,RouterLink]
})
export class LoginComponent {
  loginForm: FormGroup;
  submitted = false;
  isloggedIn: boolean = false;
  loginFailed: boolean = false;




  constructor(private formBuilder: FormBuilder, 
    private authService: AuthService,
    private router: Router,
    private jwtService: JwtService
  ) {
    this.loginForm = this.formBuilder.group({
      login: ['', Validators.required],
      password: ['', Validators.required ]
    });
  }
  

  get f() {
    return this.loginForm.controls;
  }

  togglePassword() {
    const passwordField = document.querySelector<HTMLInputElement>('#password');
    if (passwordField) {
      passwordField.type = passwordField.type === "password" ? "text" : "password";
    }
  }
  

  onSubmit() {
    console.log(this.loginForm.value);
    if (this.loginForm.valid) {
      console.log(this.loginForm);
      this.authService.login(this.loginForm.value).subscribe({
        next: (response: any) => {
          console.log(response);
          this.jwtService.storeUserInfo(response); // Store user info if needed
          this.authService.saveToken(response.jwt); // Save the token in localStorage
          this.isloggedIn = true ;
          console.log(this.authService.getToken()?.toString());
          console.log(this.isloggedIn );
          // After the login is successful, navigate to the home page
          this.redirect();
        },
        error: (err: HttpErrorResponse) => {
          if (err.status === 403) {
            this.loginFailed = true;
            console.error("Accès refusé. Vérifiez vos informations d'identification.");
          } else {
            this.loginFailed = true;
            console.error('Erreur de connexion:', err.error.message || err.statusText);
          }
        },
      });
    }
  }


  redirect(){
    if (localStorage.getItem("userRole") == "ADMIN" || 
        localStorage.getItem("userRole") == "RESPONSABLE_SAV" ||
        localStorage.getItem("userRole") == "TECHNICIEN")
      {
        this.router.navigate(['/acceuil']);
        location.href = '/dashboard'; // ou route selon rôle
      }
        else 
    this.router.navigate(['/client']);
  }


  logout() {
    this.authService.logout() ;
    this.authService.removeToken();
    this.authService.removeRefreshToken();
    this.jwtService.clearUserInfo();
    
    this.isloggedIn = false;  // Mettre à jour isloggedIn sur déconnexion
    this.router.navigate(['/login']).then(() => {
      window.location.reload();
    });
  }
  
}
