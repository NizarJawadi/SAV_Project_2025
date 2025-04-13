import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ReclamationService } from '../../../services/reclamationService';

@Component({
  selector: 'app-reclamation',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reclamation.component.html',
  styleUrl: './reclamation.component.css'
})
export class ReclamationComponent implements OnInit {
  reclamations: any[] = []; // Initialisation vide
  UserId: any ;
  constructor(private reclamationService: ReclamationService) {}

  ngOnInit(): void {
  this.UserId = localStorage.getItem("UserId"); // 🔹 Assurez-vous que UserId est défini avant l'appel
  if (this.UserId) {
    this.loadReclamations();
  } else {
    console.error("UserId non défini dans le localStorage !");
  }
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
