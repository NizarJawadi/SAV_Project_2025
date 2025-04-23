import { Component, HostListener } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth-service.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'SAV_Front';

  constructor(private router: Router, private authService: AuthService) {}

  @HostListener('window:popstate', ['$event'])
  onPopState(event: any) {
    const token = localStorage.getItem('token');
    if (!token) {
      this.router.navigate(['/login'], { replaceUrl: true });
    }
  }
}
