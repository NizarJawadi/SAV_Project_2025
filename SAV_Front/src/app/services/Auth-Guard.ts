import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const AuthGuard = () => {
  const router = inject(Router);
  const isAuthenticated = !!localStorage.getItem('token'); // Vérifie l'authentification

  if (!isAuthenticated) {
    router.navigate(['/login']); // Redirige si non authentifié
    return false;
  }
  return true; // Autorise l'accès si authentifié
};
