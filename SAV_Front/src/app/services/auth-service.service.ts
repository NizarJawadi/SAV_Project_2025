import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  REFRESH_TOKEN_KEY = 'refresh_token';
  private baseUrlAuth = 'http://localhost:8888/auth'; // Your backend URL for login
  private baseUrlRegister = 'http://localhost:8888'; // Your backend URL for register


  constructor(private http: HttpClient ) {}

  login(credentials: { login: string, password: string }): Observable<any> {
    return this.http.post(`${this.baseUrlAuth}/login`, credentials, {
      headers: new HttpHeaders({  'Content-Type': 'application/json',
        'Accept': 'application/json', }) 
    });
    
  }
  

  register(userData: any): Observable<any> {
    const url = `${this.baseUrlRegister}/client`;
    return this.http.post(url, userData); // Envoi de la requête POST
  }

  // Logout method
  logout() {
    return this.http.post(`${this.baseUrlAuth}/logout`, {}).subscribe({
      next: () => {
        // Supprime le token local (si nécessaire)
        localStorage.removeItem('token');
        console.log('Déconnexion réussie');
      },
      error: (err) => {
        console.error('Erreur lors de la déconnexion :', err);
      },
    });
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
}
