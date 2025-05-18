import { Injectable } from '@angular/core';
import * as JsSIP from 'jssip';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SipService {
  private ua: JsSIP.UA | null = null;
  private currentSession: any = null;
  public isCallActive = false;
  public isCalling = false;
  public onIncomingCall = new Subject<string>();

  // Ajoutez ces propriétés à votre service
public callDuration = 0;
private callTimer: any = null;

public outgoingCall = new Subject<{target: string, status: 'calling' | 'connected' | 'ended'}>();
   ringtone = new Audio('assets/ringtone.mp3');


  constructor() {}

  // Méthode pour vérifier la connexion
isConnected(): boolean {
  return this.ua !== null && this.ua.isConnected();
}

  async register(sipNumber: string) {
    const socket = new JsSIP.WebSocketInterface('ws://192.168.66.136:8088/ws'); // WebSocket Asterisk
    const configuration = {
      sockets: [socket],
      uri: `sip:${sipNumber}@192.168.66.136`,
      password: '1234',
      registrar_server: 'sip:192.168.66.136',
      display_name: 'Agent SAV 7001',
      session_timers: false
    };

    this.ua = new JsSIP.UA(configuration);
    this.ua.start();

    this.ua.on('connected', () => console.log('✅ Connected to Asterisk'));
    this.ua.on('registered', () => console.log('✅ SIP user registered'));
    this.ua.on('registrationFailed', (e) => console.error('❌ Registration failed:', e));

    // Gestion des appels entrants
    this.ua.on('newRTCSession', async (data: { session: any }) => {
      const session = data.session;
      this.currentSession = session;

      if (session.direction === 'incoming') {
        console.log('📞 Incoming call from', session.remote_identity.uri.toString());


        // Extraire le numéro de l'appelant
        const callerUri = session.remote_identity.uri.toString();
        const callerNumber = callerUri.match(/sip:(\d+)@/)?.[1] || callerUri;
    
    // Émettre l'événement d'appel entrant
    this.onIncomingCall.next(callerNumber);
        //const ringtone = new Audio('assets/ringtone.mp3');
        this.ringtone.loop = true;
        await this.ringtone.play();
        try {
          await this.ringtone.play();
        } catch (err) {
          console.error('Erreur lecture sonnerie', err);
        }

        // Ajout de la gestion audio
        this.attachRemoteAudio(session);

        session.on('confirmed', () => {
          console.log('✅ Call answered by remote party');
          this.isCallActive = true;
          this.isCalling = false;
          this.startCallTimer(); // Démarre le chrono
          this.ringtone.pause();
        });

         // Gestion des événements de terminaison
         session.on('failed', (e: any) => {
          console.log('Call failed:', e);
          this.onIncomingCall.next(''); // Envoie un événement vide pour fermer la popup

          this.cleanupCall();
        });
      
        session.on('ended', (e: any) => {
          console.log('Call ended:', e);
          this.cleanupCall();
        });
      
        session.on('bye', () => {
          console.log('Received BYE from remote party');
          this.onIncomingCall.next(''); // Envoie un événement vide pour fermer la popup

          this.cleanupCall();
        });

        // Auto-réponse ou bouton dans l'interface (à personnaliser)
        /*try {
          await session.answer({
            mediaConstraints: { audio: true, video: false }
          });

          console.log('✅ Call answered');
          ringtone.pause();

          this.isCallActive = true;
          this.isCalling = false;
        } catch (err) {
          console.error('❌ Error answering call', err);
        }*/
      }
    });
  }

  handleIncomingCall(caller: string) {
    this.onIncomingCall.next(caller);
  }

  async makeCall(target: string) {
    if (!this.ua) return;

    this.isCalling = true;
    this.outgoingCall.next({target, status: 'calling'});


    const session = this.ua.call(`sip:${target}@192.168.66.136`, {
      mediaConstraints: { audio: true, video: false },
      rtcOfferConstraints: {
        offerToReceiveAudio: true,
        offerToReceiveVideo: false
      }
    });

    this.currentSession = session;

    session.on('ended', () => {
      console.log('📴 Call ended');
      this.outgoingCall.next({target, status: 'ended'});
      this.isCallActive = false;
      this.isCalling = false;
      this.currentSession = null; // Important pour éviter les références circulaires

    });

    session.on('failed', () => {
      console.log('❌ Call failed');
      this.outgoingCall.next({target, status: 'ended'});
      this.isCallActive = false;
      this.isCalling = false;
      this.currentSession = null; // Important pour éviter les références circulaires

    });

    session.on('confirmed', () => {
    console.log('✅ Call confirmed');
    this.outgoingCall.next({target, status: 'connected'});
    this.isCallActive = true;
    this.isCalling = false;
    this.startCallTimer(); // Ajoutez cette ligne
  });

    this.attachRemoteAudio(session);
    this.currentSession = session;
  }

  
     
    async answerCall() {
      if (this.currentSession) {
        const session = this.currentSession;
    
        // Vérifie que c'est bien un appel entrant
        if (session.direction === 'incoming') {
          try {
            await session.answer({
              mediaConstraints: { audio: true, video: false }
            });
            this.startCallTimer(); // Démarre le chrono immédiatement

            console.log('✅ Call answered');
            this.ringtone?.pause();
            this.isCallActive = true;
            this.isCalling = false;
          } catch (err) {
            console.error('❌ Error answering call', err);
          }
        } else {
          console.warn('⚠️ Cannot answer: not an incoming call');
        }
      } else {
        console.error('❌ No current session found to answer');
      }
    }
  
    async hangUp() {
      if (this.currentSession) {
        try {
          console.log('Attempting to terminate call...');
          
          // Solution 1: Utilisez la méthode terminate() avec options
          await this.currentSession.terminate({
            status_code: 486,  // Busy Here
            reason_phrase: 'User ended call'
          });
          
          // Solution alternative si la première ne fonctionne pas
          // this.currentSession.terminate();
          
          console.log('Call terminated successfully');
        } catch (err) {
          console.error('Error terminating call:', err);
          
          // Solution de secours - force la terminaison
          try {
            this.currentSession.terminate();
          } catch (finalErr) {
            console.error('Final termination attempt failed:', finalErr);
          }
        } finally {
          this.cleanupCall();
        }
      }
    }
    
    private cleanupCall() {
      this.stopCallTimer();
      this.isCallActive = false;
      this.isCalling = false;
      this.ringtone.pause();
      this.currentSession = null;
      
      // Émettez un événement si nécessaire
      if (this.outgoingCall) {
        this.outgoingCall.next({target: '', status: 'ended'});
      }
    }

  private attachRemoteAudio(session: any) {
    session.on('peerconnection', (data: any) => {
      const peerconnection = data.peerconnection;
  
      peerconnection.addEventListener('track', (event: RTCTrackEvent) => {
        console.log('🎤 Audio track received');
        const remoteAudio = new Audio();
        remoteAudio.srcObject = event.streams[0];
        remoteAudio.autoplay = true;
        remoteAudio.play().catch(err => {
          console.error('❌ Error playing remote audio', err);
        });
      });
      
    });
  }



// Ajoutez ces méthodes pour gérer le chrono
startCallTimer() {
  this.callDuration = 0;
  this.callTimer = setInterval(() => {
    this.callDuration++;
  }, 1000);
}

stopCallTimer() {
  if (this.callTimer) {
    clearInterval(this.callTimer);
    this.callTimer = null;
  }
}

getFormattedDuration(): string {
  const minutes = Math.floor(this.callDuration / 60);
  const seconds = this.callDuration % 60;
  return `${minutes}:${seconds.toString().padStart(2, '0')}`;
}
  
}
