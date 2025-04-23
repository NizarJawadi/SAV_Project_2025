import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router } from "@angular/router";
import { AuthService } from "./auth-service.service";

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const expectedRoles: string[] = route.data['expectedRoles'] || [route.data['expectedRole']];
    const token = this.authService.getToken();

    if (!token) {
      this.router.navigate(['/unauthorized']);
      return false;
    }

    const userRole = this.authService.getUserRoleFromToken(token);

    // Si userRole est null, empêcher l'accès
    if (userRole === null || !expectedRoles.includes(userRole)) {
      this.router.navigate(['/unauthorized']);
      return false;
    }

    return true;
  }
}
