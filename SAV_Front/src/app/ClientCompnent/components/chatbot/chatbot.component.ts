import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { WebSocketService } from '../../../services/chatbot.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatBadgeModule } from '@angular/material/badge';

@Component({
  selector: 'app-chatbot',
  standalone: true,
  imports: [
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatBadgeModule,
    FormsModule,
    CommonModule
  ],
  templateUrl: './chatbot.component.html',
  styleUrl: './chatbot.component.scss'
})
export class ChatbotComponent implements OnInit, OnDestroy {
  public messages: { text: string; user: string; avatar: string; time: string }[] = [];  // Typage amélioré pour les messages
  public messageText: string = '';  // Texte du message à envoyer
  private userId: number  = 2; // ID de l'utilisateur (peut être dynamique si nécessaire)
  public isMinimized: boolean = false; // État du chat (minimisé ou non)
  private messagesSubscription!: Subscription;


  isBotTyping = false;

  constructor(private webSocketService: WebSocketService) {}

  ngOnInit() {
    this.webSocketService.connect();
  
    this.messagesSubscription = this.webSocketService.receivedMessages.subscribe((message: string) => {
      setTimeout(() => {
        // ➤ Affiche la réponse après un délai pour rendre l'effet réaliste
        this.messages.push({
          text: message,
          user: 'Bot',
          avatar: './assets/POE-Logo.png',
          time: new Date().toLocaleTimeString()
        });
        this.isBotTyping = false; // ➤ Cache l'animation après réception
      }, 1000); // par exemple, 1 seconde
    });
  }
  


  formatText(text: string): string {
    // Remplacer chaque point suivi d'un espace par un point et un saut de ligne
    return text.replace(/\.(\s*)/g, ".<br>");
 }

  
  ngOnDestroy() {
    // Déconnexion du WebSocket et annulation de la souscription lors de la destruction du composant
    this.webSocketService.disconnect();
    this.messagesSubscription.unsubscribe();
  }

  // Méthode pour envoyer un message
  public sendMessage() {
    if (this.messageText.trim()) {
      const message = {
        text: this.messageText,
        user: 'You',
        avatar: '',
        time: new Date().toLocaleTimeString()
      };
  
      this.webSocketService.sendMessage(this.messageText, this.userId);
      this.messages.push(message);
      this.messageText = '';
      
      this.isBotTyping = true; // ➤ Active l'animation après envoi
    }
  }
  
  getCurrentTime(): string {
    return new Date().toLocaleTimeString();
  }
  

  // Toggle pour minimiser/agrandir le chat
  public toggleMinimize() {
    this.isMinimized = !this.isMinimized;
  }
}
