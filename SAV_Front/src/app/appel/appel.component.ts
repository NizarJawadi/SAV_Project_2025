import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SipService } from '../services/SipService';

@Component({
  selector: 'app-appel',
  standalone: true,
  imports: [ FormsModule, CommonModule],
  templateUrl: './appel.component.html',
  styleUrl: './appel.component.css'
})
export class AppelComponent {
 MySIP: any;
 target: any ;
  public isCallActive = false;
  public isCalling = false;
  public callEnded = false;
  public incomingCall = false;
  public incomingCaller = '';

  public outgoingCallData: {target: string, status: 'calling' | 'connected' | 'ended'} | null = null;

  constructor(public voipService: SipService) {
    // Écoute des appels entrants
    this.voipService.onIncomingCall.subscribe((caller: string) => {
      if (caller === '') {
        // Fermeture forcée de la popup
        this.incomingCall = false;
        this.callEnded = true;
        setTimeout(() => this.closePopup(), 1000);
      } else {
        this.incomingCall = true;
      this.incomingCaller = caller;
      this.callEnded = false;
      }
    });

    // Appels sortants
      this.voipService.outgoingCall.subscribe(call => {
        this.outgoingCallData = call;
        if (call.status === 'ended') {
          this.callEnded = true;
        }
      });
  }

  init() {
    this.MySIP = localStorage.getItem("phoneSIP") || '0000' ;
    this.voipService.register(this.MySIP);
  }

  makeCall() {
    if (this.target) {
      this.voipService.makeCall(this.target);
    } else {
      console.warn('Veuillez saisir une extension.');
    }
  }
  
  async hangUp() {
    try {
      await this.voipService.hangUp();
      this.callEnded = true;
      this.isCallActive = false;
      
      // Ajoutez un feedback visuel
      if (this.outgoingCallData) {
        this.outgoingCallData.status = 'ended';
      }
      
      // Ferme automatiquement après 3 secondes
      setTimeout(() => {
        if (this.outgoingCallData) {
          this.closeOutgoingPopup();
        }
        if (this.incomingCall) {
          this.closePopup();
        }
      }, 3000);
    } catch (err) {
      console.error('Error in hangUp:', err);
      // Force la fermeture même en cas d'erreur
      this.closeOutgoingPopup();
      this.closePopup();
    }
  }
  
  answerCall() {
    this.voipService.answerCall();
    this.callEnded = false;
    this.isCallActive = true;
    this.voipService.startCallTimer();
  }
  
  declineCall() {
    this.voipService.hangUp();
    this.incomingCall = false;
    this.callEnded = false;
    this.voipService.stopCallTimer(); // Arrête le chrono si jamais il était démarré

  }

  closePopup() {
    this.incomingCall = false;
    this.callEnded = false;
    this.voipService.stopCallTimer();
  }

  // Méthode pour formater le temps (optionnel - peut aussi utiliser le service)
  get callDuration() {
    return this.voipService.getFormattedDuration();
  }

  closeOutgoingPopup() {
    this.outgoingCallData = null;
    this.callEnded = false;
    this.voipService.stopCallTimer();
  }
}