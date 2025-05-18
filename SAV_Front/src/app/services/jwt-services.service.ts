import { Injectable } from '@angular/core';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class JwtService {
  constructor() {}

  decodeToken(token: string): any {
    try {
      if (!token || typeof token !== 'string') {
        throw new Error('Token must be a non-empty string.');
      }
      return jwtDecode(token);
    } catch (error) {
      console.error('Invalid token:', error);
      return null;
    }
  }

  clearUserInfo(): void {
    localStorage.removeItem('userRole');
    localStorage.removeItem('UserName');
    localStorage.removeItem('token');
    localStorage.removeItem('tokenExpiration');
    console.log('User information cleared from localStorage.');
  }

  getUserRole(): string | null {
    const role = localStorage.getItem('userRole');
    if (role) {
      return role;
    } else {
      console.warn('No user role found in localStorage.');
      return null;
    }
  }

  isTokenExpired(): boolean {
    const tokenExpiration = localStorage.getItem('tokenExpiration');
    if (!tokenExpiration) {
      return true;
    }

    const currentTime = Math.floor(Date.now() / 1000); // Current time in seconds
    return currentTime >= parseInt(tokenExpiration, 10);
  }

  storeUserInfo(response: any): void {
    const token = response?.jwt;
    if (!token || typeof token !== 'string') {
      console.error('Invalid token provided for storage.');
      return;
    }

    const decodedToken = this.decodeToken(token);
    if (decodedToken) {
      // Assuming 'sub' is the user name
      const userName = decodedToken.sub || '';
      const phoneSIP = decodedToken.phoneSIP || '';
      localStorage.setItem('UserName', userName);
      localStorage.setItem('phoneSIP', phoneSIP);
      // Stockage du rôle utilisateur (directement depuis 'role')
      const userRole = decodedToken.role || '';
      if (userRole) {
        localStorage.setItem('userRole', userRole);
      }

      // Stockage de la date d'expiration
      if (decodedToken.exp) {
        localStorage.setItem('tokenExpiration', decodedToken.exp.toString());
      }

      // Stockage de id user
      const idUser = decodedToken.idUser || '';
      localStorage.setItem('UserId', idUser );

      // Stockage du token
      localStorage.setItem('token', token);
    } else {
      console.error('Failed to decode token.');
    }
  }
}
