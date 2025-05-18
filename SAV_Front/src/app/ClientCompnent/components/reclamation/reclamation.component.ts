import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ReclamationService } from '../../../services/reclamationService';
import { SipService } from '../../../services/SipService';
import Swal from 'sweetalert2';
import { AppelComponent } from '../../../appel/appel.component';

@Component({
  selector: 'app-reclamation',
  standalone: true,
  imports: [CommonModule , AppelComponent],
  templateUrl: './reclamation.component.html',
  styleUrl: './reclamation.component.css'
})
export class ReclamationComponent implements OnInit {
  reclamations: any[] = []; // Initialisation vide
  UserId: any ;

  mySip = localStorage.getItem('phoneSIP') || '0000';

  constructor(private reclamationService: ReclamationService,
    private sipService: SipService
  ) {}

  ngOnInit(): void {
  this.UserId = localStorage.getItem("UserId"); // 🔹 Assurez-vous que UserId est défini avant l'appel
  if (this.UserId) {
    this.loadReclamations();
  } else {
    console.error("UserId non défini dans le localStorage !");
  }

  this.sipService.register(this.mySip);
}


appelerResponsable(sipNumber: string) {
    // Vérifier si l'utilisateur est connecté au SIP
     const mySip = localStorage.getItem('phoneSIP') || '0000';
    if (!this.sipService.isConnected()) {
      this.sipService.register(mySip).then(() => {
        this.lancerAppel(sipNumber);
      });
    } else {
      this.lancerAppel(sipNumber);
    }
  }

   private lancerAppel(sipNumber: string) {
    // Stocker le numéro SIP du responsable dans le localStorage
    localStorage.setItem('currentCallSip', sipNumber);
    
    // Lancer l'appel
    this.sipService.makeCall(sipNumber);
    
    // Vous pouvez aussi ouvrir une popup de confirmation
    Swal.fire({
      title: 'Appel en cours',
      text: `Appel vers le responsable SAV (${sipNumber})`,
      icon: 'info',
      showCancelButton: true,
      confirmButtonText: 'Raccrocher',
      cancelButtonText: 'Continuer'
    }).then((result) => {
      if (result.isConfirmed) {
        this.sipService.hangUp();
      }
    });
  }

  loadReclamations(): void {
    this.reclamationService.getReclamationsByClient(this.UserId).subscribe(
      (data) => {
        this.reclamations = data;
        console.log(data)
      },
      (error) => {
        console.error('Erreur lors du chargement des réclamations', error);
      }
    );
  }

  annulerReclamation(reclamationId: number): void {
    if (confirm('Êtes-vous sûr de vouloir annuler cette réclamation ?')) {
      this.reclamationService.annulerReclamation(reclamationId).subscribe(
        (response) => {
          console.log('Réclamation annulée:', response);
          this.loadReclamations();  // Recharger les réclamations après annulation
        },
        (error) => {
          console.error('Erreur lors de l\'annulation de la réclamation', error);
        }
      );
    }
  }

  getStatusClass(statut: string) {
    switch (statut?.toLowerCase()) {
      case 'approuvé': return 'badge bg-success';
      case 'en attente': return 'badge bg-warning text-dark';
      case 'rejeté': return 'badge bg-danger';
      default: return 'badge bg-secondary';
    }
  }

  
}
