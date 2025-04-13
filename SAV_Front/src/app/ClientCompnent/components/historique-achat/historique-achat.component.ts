import { CommonModule } from '@angular/common';
import { Component, NgModule, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, NgModel, ReactiveFormsModule, Validators } from '@angular/forms';
import { HistoriqueAchatService } from '../../../services/historique-achat';
import * as bootstrap from 'bootstrap';
import { ReclamationService } from '../../../services/reclamationService';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-historique-achat',
  standalone: true,
  imports: [CommonModule , ReactiveFormsModule , FormsModule ],
  templateUrl: './historique-achat.component.html',
  styleUrl: './historique-achat.component.css'
})
export class HistoriqueAchatComponent implements OnInit {
  // Définir les données statiques pour l'historique des achats
  achats: any[] = []; // Initialiser un tableau pour stocker les achats
  isLoading: boolean = true; // État de chargement
  error: string = ''; // Message d'erreur
  userId: number = 0; // Par exemple, ID d'un utilisateur (ça peut être dynamique)

  reclamationForm: FormGroup;
  produitReclame: any = null;


  constructor(private historiqueAchatService: HistoriqueAchatService,
    private fb: FormBuilder,
    private modalService: NgbModal,
    private reclamationService: ReclamationService,

  ) {
    this.reclamationForm = this.fb.group({
      description: ['', Validators.required]
    });
   }

  ngOnInit(): void {
    this.userId = parseInt(localStorage.getItem("UserId") || '0', 10);
    this.getHistoriqueAchats();

  }

  isUnderWarranty(garantieExpireLe: string | null): boolean {
    if (!garantieExpireLe) return false; // Si aucune date de garantie, alors le produit n'est pas sous garantie
    const expiryDate = new Date(garantieExpireLe);
    const currentDate = new Date();
    return expiryDate >= currentDate;
  }
  

   // Méthode pour récupérer l'historique des achats
   getHistoriqueAchats(): void {
    this.historiqueAchatService.getHistoriqueAchats(this.userId).subscribe({
      next: (data) => {
        this.achats = data; // Stocker les données récupérées dans le tableau achats
        this.isLoading = false; // Fin du chargement
        console.log(this.achats)
      },
      error: (err) => {
        this.error = 'Erreur lors de la récupération des achats'; // Gérer l'erreur
        this.isLoading = false;
      }
    });
  }


  ouvrirModalReclamation(achat: any) {
    this.produitReclame = achat;
    setTimeout(() => {
      const modalElement = document.getElementById('reclamationModal');
      if (modalElement) {
        const modal = new bootstrap.Modal(modalElement);
        modal.show();
      }
    }, 0);
  }
  
  

  ajouterReclamation() {
    if (this.reclamationForm.valid) {
      const reclamation = {
        produitId: this.produitReclame.produitId,
        description: this.reclamationForm.value.description,
        clientId: this.userId
      };
  
      this.reclamationService.ajouterReclamation(reclamation).subscribe({
        next: () => {
          alert('Réclamation envoyée avec succès !');
          this.reclamationForm.reset();
          const modalElement = document.getElementById('reclamationModal');
          if (modalElement) {
            const modal = bootstrap.Modal.getInstance(modalElement);
            modal?.hide();
          }
        },
        error: () => {
          alert('Erreur lors de l\'envoi de la réclamation.');
        }
      });
    }
  }
  

  
}