import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth-service.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  isDropdownOpen = false;
  username : any ;
  

  constructor (private authServices: AuthService ,
         private router: Router,
   ){
    
   }
  ngOnInit(): void {
    this.getUsername() ;

}
  
  getUsername(): void {
    const username = localStorage.getItem('UserName');
    this.username = username ;
  }

  logout() {
    this.authServices.logout(); // si tu as besoin d'appeler le backend pour déconnexion
    localStorage.clear(); // supprime tout
    this.router.navigate(['/login'], { replaceUrl: true });
  }
  

  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
}
}
