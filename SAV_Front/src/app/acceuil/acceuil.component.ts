import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-acceuil',
  standalone: true,
  imports: [HeaderComponent,RouterLink , RouterOutlet],
  templateUrl: './acceuil.component.html',
  styleUrl: './acceuil.component.css'
})
export class AcceuilComponent {
  isLoggedIn = false ; 
 
}
