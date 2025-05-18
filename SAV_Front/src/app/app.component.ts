import { Component, HostListener } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth-service.service';
import { AppelComponent } from './appel/appel.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet , AppelComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'SAV_Front';
 
   userRole : any ; 

  constructor(private router: Router, private authService: AuthService) {
      this.userRole = localStorage.getItem('userRole');

  }

  @HostListener('window:popstate', ['$event'])
  onPopState(event: any) {
    const token = localStorage.getItem('token');
    if (!token) {
      this.router.navigate(['/login'], { replaceUrl: true });
    }
  }
}
