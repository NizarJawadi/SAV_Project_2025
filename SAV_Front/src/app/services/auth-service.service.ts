import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, of } from 'rxjs';
import { JwtService } from './jwt-services.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  REFRESH_TOKEN_KEY = 'refresh_token';
  private baseUrlAuth = 'http://localhost:8888/auth'; // Your backend URL for login
  private baseUrlRegister = 'http://localhost:8888'; // Your backend URL for register


  constructor(private http: HttpClient,
    private jwtService: JwtService
   ) {}

   login(credentials: { login: string, password: string }): Observable<any> {
    return this.http.post(`${this.baseUrlAuth}/login`, credentials, {
      headers: new HttpHeaders({  
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      })
    }).pipe(
      catchError(error => {
        console.error('Login error:', error);
        throw error;
      })
    );
  }
  
  

  register(userData: any): Observable<any> {
    const url = `${this.baseUrlRegister}/client/add`;
    return this.http.post(url, userData); // Envoi de la requête POST
  }

  // Logout method
  logout() {
    this.http.post(`${this.baseUrlAuth}/logout`, {}).subscribe({
      next: () => {
        this.removeToken();
        this.removeRefreshToken();
        this.clearUserInfo();  // Ajoute une méthode pour effacer toutes les informations
        window.location.href = '/login';  // Rediriger vers la page de login
      },
      error: (err) => {
        console.error('Erreur lors de la déconnexion :', err);
      },
    });
  }
  
  clearUserInfo() {
    localStorage.removeItem('userRole');
    localStorage.removeItem('UserName');
    localStorage.removeItem('token');
    localStorage.removeItem('tokenExpiration');
    localStorage.removeItem('UserId');
    console.log('Informations utilisateur effacées du localStorage.');
  }
  
  
  getUserRoleFromToken(token: string | null = this.getToken()): string | null {
    if (!token) {
      // Token est null, donc retourner null ou faire une action si nécessaire
      return null;
    }
  
    const decoded = this.jwtService.decodeToken(token);
    return decoded?.role || null;
  }
  

  // Save token to localStorage
  saveToken(token: string | undefined) {
    if (token && token !== 'undefined') {
      localStorage.setItem('token', token); // Save valid tokens
    } else {
      this.removeToken(); // Remove token if invalid
    }
  }

  // Retrieve token from localStorage
  getToken(): string | null {
    const token = localStorage.getItem('token');
    if (token === "undefined" || token === null) {
      this.removeToken(); // Remove invalid token if found
      return null;
    }
    return token; // Return the valid token or null if not found
  }

  // Remove token from localStorage
  removeToken() {
    localStorage.removeItem('token');
  }

  // Save refresh token to localStorage
  saveRefreshToken(token: string) {
    localStorage.setItem(this.REFRESH_TOKEN_KEY, token);
  }

  // Get refresh token from localStorage
  getRefreshToken(): string | null {
    return localStorage.getItem(this.REFRESH_TOKEN_KEY);
  }

  // Remove refresh token from localStorage
  removeRefreshToken() {
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);
  }

  // Check if the user is logged in
  isLoggedIn(): boolean {
    return this.getToken() !== null; // Return true if token exists, false otherwise
  }


  
sendVerificationCode(email: string): Observable<any> {
    return this.http.post(`${this.baseUrlRegister}/client/send-code`, { email }).pipe(
      catchError(error => {
        console.error('Erreur envoi code:', error);
        return of({ error: 'Erreur lors de l\'envoi du code' });
      })
    );
  }

  verifyCode(email: string, code: string): Observable<any> {
    return this.http.post(`${this.baseUrlRegister}/client/verify-code`, { email, code }).pipe(
      catchError(error => {
        console.error('Erreur vérification code:', error);
        return of({ valid: false, message: 'Erreur serveur' });
      })
    );
  }

  requestPasswordReset(email: string): Observable<any> {
  return this.http.post(`${this.baseUrlRegister}/client/request-password-reset`, { email });
}

resetPassword(email: string, code: string, newPassword: string, confirmPassword: string): Observable<any> {
  const body = {
    email: email,
    code: code,
    newPassword: newPassword,
    confirmPassword: confirmPassword
  };

  console.log('Corps de la requête:', body); // Debug

  return this.http.post(`${this.baseUrlRegister}/client/reset-password`, body).pipe(
    catchError(error => {
      console.error('Erreur détaillée:', error);
      throw error;
    })
  );
}
  
}
