import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';

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
  
  ngOnInit(): void {
    this.getUsername() ;

}
  
  getUsername(): void {
    const username = localStorage.getItem('UserName');
    this.username = username ;
  }

  logout() {
    // Ajoutez ici la logique de déconnexion (ex: suppression du token)
    console.log("Déconnexion...");
}

  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
}
}
