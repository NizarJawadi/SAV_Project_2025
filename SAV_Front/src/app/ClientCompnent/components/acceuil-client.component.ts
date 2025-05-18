import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { MainContentComponent } from './main-content/main-content.component';
import { ChatbotComponent } from './chatbot/chatbot.component';
import { AppelComponent } from '../../appel/appel.component';

@Component({
  selector: 'app-acceuil-client',
  standalone: true,
  imports: [CommonModule ,
             RouterLink ,
             HeaderComponent ,
             FooterComponent ,
             MainContentComponent,
             ChatbotComponent,
             RouterOutlet  ,
            AppelComponent ],
  templateUrl: './acceuil-client.component.html',
  styleUrl: './acceuil-client.component.css'
})
export class AcceuilClientComponent {

  userRole : any ; 

  constructor(){
  this.userRole = localStorage.getItem('userRole');

  }
}
