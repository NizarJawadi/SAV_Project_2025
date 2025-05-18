import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ReclamationService } from '../services/reclamationService';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';
import * as bootstrap from 'bootstrap';
import { TechnicienService } from '../services/TechnicienServices';
import Swal from 'sweetalert2';
import { SipService } from '../services/SipService';
import { AppelComponent } from '../appel/appel.component';

@Component({
  selector: 'app-reclamation',
  standalone: true,
  imports: [HeaderComponent, CommonModule, ReactiveFormsModule, FormsModule ],
  templateUrl: './reclamation.component.html',
  styleUrls: ['./reclamation.component.css']
})
export class ReclamationComponent implements OnInit {
  statutFilter: string = 'all'; // Valeur par défaut
  reclamations: any[] = [];
  allReclamations: any[] = [];
  selectedReclamation: any;
  currentPage = 0;
  Math = Math;
  pageSize = 5;
  deadline: string = ''; // Format ISO, ex: '2025-04-19T15:30'


  searchNumeroSerie: string = '';


  techniciens: any[] = []; 
  specialites: string[] = []; // Assurez-vous qu'il est initialisé
  selectedSpecialite: string | null = null;
  selectedTechnicienId: number | null = null;
  currentReclamationId: any;
  responsableSAVId:any ;

   mySip = localStorage.getItem('phoneSIP') || '0000';

  constructor(private reclamationService: ReclamationService,
    private technicienService: TechnicienService,
    private cdRef: ChangeDetectorRef,
    private sipService: SipService
  ) {}

  ngOnInit(): void {
    this.loadReclamationsByStatut(this.statutFilter);
    this.loadSpecialites();
this.sipService.register(this.mySip);
  }


 appelerClient(sipNumber: string) {
  // Vérifier la connexion SIP
  if (!this.sipService.isConnected()) {
    const mySip = localStorage.getItem('phoneSIP') || '0000'; // Valeur par défaut
    if (!mySip) {
      console.error('Numéro SIP non trouvé');
      return;
    }
    
    this.sipService.register(mySip).then(() => {
      this.lancerAppelClient(sipNumber);
    });
  } else {
    this.lancerAppelClient(sipNumber);
  }
}

  private lancerAppelClient(sipNumber: string) {
    localStorage.setItem('currentCallSip', sipNumber);
    this.sipService.makeCall(sipNumber);
  }


  searchByNumeroSerie(): void {
    if (this.searchNumeroSerie.trim() === '') {
      this.reclamations = [...this.allReclamations];
    } else {
      this.reclamations = this.allReclamations.filter(reclamation =>
        reclamation.produit.numeroSerie.toLowerCase().includes(this.searchNumeroSerie.toLowerCase())
      );
    }
  }

  
  // Méthode pour charger les réclamations selon le statut
  loadReclamationsByStatut(statut: string): void {
    if (statut === 'all') {
      this.getAllReclamations();
    } else {
      this.reclamationService.getReclamationsByStatut(statut).subscribe(
        (data) => {
          this.reclamations = data;
          console.log(this.reclamations)
        },
        (error) => {
          console.error('Erreur lors de la récupération des réclamations par statut : ', error);
        }
      );
    }
  }

  loadTechniciens() {
    this.selectedTechnicienId = null;
    if (this.selectedSpecialite) {
      this.technicienService.getTechniciensBySpecialite(this.selectedSpecialite).subscribe(
        (data) => {
          this.techniciens = data;

          console.log("Techniciens chargés :", this.techniciens);

        },
        (error) => {
          console.error("Erreur lors du chargement des techniciens :", error);
        }
      );
    }
  }

  onSpecialiteChange(selectedSpecialite: string) {
    this.selectedSpecialite = selectedSpecialite;
    this.loadTechniciens();
  }

  

  loadSpecialites() {
    this.technicienService.getAllSpecialites().subscribe(
      (data) => {
        this.specialites = data;
      },
      (error) => {
        console.error("Erreur lors du chargement des spécialités :", error);
      }
    );
  }
  
  
  getAllReclamations(): void {
    this.reclamationService.getAllReclamations().subscribe(
      (data) => {
        this.allReclamations = data;
        console.log(data)
        this.reclamations = [...this.allReclamations];
      },
      (error) => {
        console.error('Erreur lors de la récupération des réclamations : ', error);
      }
    );
  }

  onTechnicienSelect(id: number) {
    this.selectedTechnicienId = id;
    console.log("Technicien sélectionné :", this.selectedTechnicienId);
  }
  

  assignTechnicien() {
    if (!this.selectedTechnicienId || !this.deadline) {
      Swal.fire({
        icon: 'warning',
        title: 'Champs requis',
        text: 'Veuillez choisir un technicien et une date limite.'
      });
      return;
    }
  
    const deadlineParam = new Date(this.deadline).toISOString(); // format ISO
    this.responsableSAVId = Number(localStorage.getItem("UserId")); 
    this.currentReclamationId = Number(this.currentReclamationId); 
    this.selectedTechnicienId = Number(this.selectedTechnicienId);
  
    this.reclamationService.assignReclamation(this.currentReclamationId, this.selectedTechnicienId, this.responsableSAVId, deadlineParam)
      .subscribe({
        next: (response: any) => {
          Swal.fire({
            icon: 'success',
            title: 'Succès',
            text: response.message // Afficher le message de la réponse JSON
          });
          // Fermer la modal ici si besoin
        },
        error: err => {
          console.error(err);
          Swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: "L'assignation a échoué."
          });
        }
      });
  }
  

  openReclamationDetails(reclamation: any): void {
    this.selectedReclamation = reclamation;
  }

  onDelete(id: number): void {
    this.reclamations = this.reclamations.filter(r => r.id !== id);
  }

  prevPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
    }
  }

  nextPage(): void {
    if ((this.currentPage + 1) * this.pageSize < this.reclamations.length) {
      this.currentPage++;
    }
  }



  openModal(reclamation: any ) {
    this.currentReclamationId = reclamation.id;
    this.selectedSpecialite = '';
    this.selectedTechnicienId = null;
    this.techniciens = [];
    const modalElement = document.getElementById('assignModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  }


  ouvrirFacture(reclamationId: number): void {
  const url = `http://localhost:8888/factures/export/${reclamationId}`;
  window.open(url, '_blank');
}

}
