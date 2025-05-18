import { Injectable } from '@angular/core';
import { Client, Message } from '@stomp/stompjs';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private client: Client;
  private serverUrl = 'ws://localhost:1080/chat'; // URL de l'endpoint WebSocket
  private messagesChannel = '/topic/messages'; // Canal où les messages sont envoyés
  public receivedMessages: Subject<string> = new Subject<string>();  // Sujet pour émettre les messages reçus

  constructor() {
    this.client = new Client({
      brokerURL: this.serverUrl,
      connectHeaders: {},
      debug: (str) => {
        console.log(str); // Pour afficher le debug WebSocket
      },
      onConnect: () => {
        console.log("Connected to WebSocket server");
        this.subscribeToMessages();  // Souscrire après la connexion
      },
      onStompError: (frame) => {
        console.error('STOMP error: ', frame);
      }
    });
  }

  // Connexion WebSocket
  public connect() {
    this.client.activate();
  }

  // Déconnexion WebSocket
  public disconnect() {
    this.client.deactivate();
  }

  // Envoi du message au serveur WebSocket
  public sendMessage(content: string, userId: number) {
    if (this.client.connected) {
      const payload = {
        message: content,
        userId: userId
      };
  
      this.client.publish({
        destination: '/app/send',
        body: JSON.stringify(payload),
        headers: { 'content-type': 'application/json' }
      });
    }
  }
  

  // Méthode rendue publique pour pouvoir l'appeler dans le composant
  public subscribeToMessages() {
    if (this.client.connected) {
      this.client.subscribe(this.messagesChannel, (message: Message) => {
        console.log("Message received:", message.body);
        
        // Émettre le message reçu via le sujet
        this.receivedMessages.next(message.body);
      });
    }
  }
}
