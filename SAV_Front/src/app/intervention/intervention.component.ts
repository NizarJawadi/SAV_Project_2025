import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { InterventionService } from '../services/InterventionService';
import { PieceDeRechangeService } from '../services/PieceDeRechangeService';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HeaderComponent } from '../header/header.component';
import { CdkDragDrop, DragDropModule, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import * as bootstrap from 'bootstrap';
import Swal from 'sweetalert2';
import { ToastrModule, ToastrService } from 'ngx-toastr';



@Component({
  selector: 'app-intervention',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, HeaderComponent, DragDropModule , ],
  templateUrl: './intervention.component.html',
  styleUrls: ['./intervention.component.css']
})
export class InterventionComponent implements OnInit {
  modalIntervention: any = null;
  modalReclamation: any = null;
  modalClient: any = null;

  
  interventions: any[] = [];
  enAttente: any[] = [];
  enCours: any[] = [];
  terminees: any[] = [];
  showGroupedView = false;

  recherche: string = '';
  piecesDisponibles: any[] = [];
  piecesFiltrees: any[] = [];
  piecesSelectionnees: any[] = [];
  piecesUtilisees: any[] = [];

  selectedInterventionId!: number;


  filteredInterventions: any ;
  searchDescription: string = '';
searchStatut: string = '';
  searchMatricule: any;

  constructor(
    private interventionService: InterventionService,
    private pieceDeRechange: PieceDeRechangeService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    //this.fetchInterventions();
    this.filteredInterventions = [];
    this.loadInterventions()
    this.chargerPieces();
    this.toastr.success('Opération réussie');
this.toastr.warning('Attention à la deadline');
this.toastr.error('Erreur survenue');
this.toastr.info('Info simple');
  }


  loadInterventions(): void {
    // Par exemple, récupérer par spécialité ou matricule selon le rôle
    if (localStorage.getItem('userRole') == "TECHNICIEN") {
      const idTechnicien: any = localStorage.getItem('UserId'); // à implémenter dans AuthService
      this.interventionService.getInterventionsByUserId(idTechnicien).subscribe((data: any[]) => {
        this.interventions = data;
        console.log(this.interventions)
        this.checkForDeadlines(data);
        this.filterInterventions(); // <- AJOUT ICI

       // this.filteredInterventionsByTech = data;
       // console.log(this.filteredInterventionsByTech)

      });
    } else {
     this.fetchInterventions() ;

    }
  }

  isTechnicien(): boolean {
    return localStorage.getItem('userRole') === 'TECHNICIEN';
  }

  fetchInterventions(): void {
    this.interventionService.getAllInterventions().subscribe(data => {
      if (data && Array.isArray(data)) {
        this.interventions = data;
        console.log(this.interventions)
        this.filterInterventions();
        this.filterInterventionsResponsable(); // ✅ ajoute ça

      } else {
        console.error('Données mal formatées:', data);
      }
    });
  }

  filterInterventions(): void {
    this.enAttente = this.interventions.filter(i => i.intervention.statut === 'EN_ATTENTE');
    this.enCours = this.interventions.filter(i => i.intervention.statut === 'EN_COURS');
    this.terminees = this.interventions.filter(i => i.intervention.statut === 'TERMINEE');
  }

  filterInterventionsResponsable(): void {
    this.filteredInterventions = this.interventions.filter(i => {
      const matchesStatut = this.searchStatut
        ? i.intervention.statut === this.searchStatut
        : true;
  
      const matchesMatricule = this.searchMatricule
        ? i.technicien?.matricule?.toLowerCase().includes(this.searchMatricule.toLowerCase())
        : true;
  
      return matchesStatut && matchesMatricule;
    });
  }

  checkForDeadlines(interventions: any[]): void {
    const now = new Date();
    const threshold = new Date();
    threshold.setHours(now.getHours() + 24);
  
    interventions.forEach(intervention => {
      const deadline = new Date(intervention.intervention.dateDeadLine);
      const client = intervention.client.username;
      const technicien = intervention.technicien.username;
      const interventionDescription = intervention.reclamation.description;
      
      if (deadline < now) {
        // Notification avec SweetAlert2 pour un retard
        Swal.fire({
          title: 'Intervention en retard',
          text: `L'intervention pour le client ${client} est en retard ! Le technicien assigné est ${technicien}. Date limite : ${deadline.toLocaleString()}.`,
          icon: 'warning',
          confirmButtonText: 'OK'
        });
      } else if (deadline < threshold) {
        // Notification avec SweetAlert2 pour une intervention proche de la date limite
        Swal.fire({
          title: 'Date limite proche',
          text: `L'intervention pour le client ${client} approche de la date limite. Le technicien assigné est ${technicien}. Date limite : ${deadline.toLocaleString()}.`,
          icon: 'info',
          confirmButtonText: 'OK'
        });
      }
    });
  }
  
  
  
  
  

  toggleView(): void {
    this.showGroupedView = !this.showGroupedView;
  }

  viewDetails(intervention: any) {
    this.modalIntervention = intervention.intervention;
    this.modalReclamation = intervention.reclamation;
    this.modalClient = intervention.client;
    console.log(intervention)
        // Afficher le modal via Bootstrap
    const modalElement = document.getElementById('interventionModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  }
  
  formatStatut(statut: string): string {
    switch (statut) {
      case 'EN_ATTENTE': return 'En attente';
      case 'EN_COURS': return 'En cours';
      case 'TERMINEE': return 'Terminée';
      default: return statut;
    }
  }
  
  onDrop(event: CdkDragDrop<any[]>, listName: string): void {
    const movedData = event.item.data;
    if (!movedData || !movedData.intervention) {
      Swal.fire({
        icon: 'error',
        title: 'Erreur',
        text: 'Objet non valide pour le drag & drop.'
      });
      return;
    }
  
    const movedIntervention = movedData.intervention;
    const nouveauStatut = this.getStatusFromList(listName);
    movedIntervention.statut = nouveauStatut;
  
    this.interventionService.updateInterventionStatus(movedIntervention.id, nouveauStatut).subscribe({
      next: () => {
        if (event.previousContainer === event.container) {
          moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
        } else {
          transferArrayItem(event.previousContainer.data, event.container.data, event.previousIndex, event.currentIndex);
        }
  
        Swal.fire({
          icon: 'success',
          title: 'Statut mis à jour',
          text: `L'intervention a été déplacée vers "${this.formatStatut(nouveauStatut)}".`,
          timer: 1500,
          showConfirmButton: false
        });
      },
      error: err => {
        console.error('Erreur de mise à jour:', err);
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: "Impossible de mettre à jour le statut de l'intervention."
        });
      }
    });
  }
  

  getStatusFromList(listName: string): string {
    switch (listName) {
      case 'enAttente': return 'EN_ATTENTE';
      case 'enCours': return 'EN_COURS';
      case 'terminees': return 'TERMINEE';
      default: return 'INCONNU';
    }
  }

  openAddPiecesModal(intervention: any) {
    this.selectedInterventionId = intervention.intervention.id;
    this.resetSelection();

     // Appel à un service pour récupérer les pièces utilisées pour cette intervention
     this.interventionService.getPiecesUsedForIntervention(this.selectedInterventionId).subscribe(data => {
      this.piecesUtilisees = data;  // Mettre à jour les pièces utilisées
      console.log(data);
    });


    const modalElement = document.getElementById('addPiecesModal');
    if (modalElement) {
      const modal = new window.bootstrap.Modal(modalElement);
      modal.show();
    }
  }

  chargerPieces(): void {
    this.pieceDeRechange.getAllPieces().subscribe(data => {
      this.piecesDisponibles = data.map(p => ({ ...p, selected: false, quantite: 1 }));
      this.piecesFiltrees = [...this.piecesDisponibles];
    });
  }

  filtrerPieces(): void {
    const terme = this.recherche.toLowerCase();
    this.piecesFiltrees = this.piecesDisponibles.filter(piece =>
      piece.nom.toLowerCase().includes(terme) || piece.reference.toLowerCase().includes(terme)
    );
  }

  onPieceSelectionChange(piece: any): void {
    if (piece.selected) {
      if (!this.piecesSelectionnees.find(p => p.id === piece.id)) {
        piece.quantite = 1;
      
        this.piecesSelectionnees.push(piece);
      }
    } else {
      this.piecesSelectionnees = this.piecesSelectionnees.filter(p => p.id !== piece.id);
      console.log(this.piecesSelectionnees)
    }
  }

  supprimerPiece(piece: any): void {
    piece.selected = false;
    this.piecesSelectionnees = this.piecesSelectionnees.filter(p => p.id !== piece.id);
  }

  ajouterPieces(): void {
    if (!this.selectedInterventionId || this.piecesSelectionnees.length === 0) {
      Swal.fire({
        icon: 'warning',
        title: 'Avertissement',
        text: 'Veuillez sélectionner au moins une pièce.',
      });
      return;
    }
  
    const payload = this.piecesSelectionnees.map(p => ({
      pieceId: p.id,
      quantite: p.quantite,
      prixUnitaire: p.prix,
      pieceName: p.nom
    }));
  
    this.interventionService.ajouterPieces(this.selectedInterventionId, payload).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: 'Succès',
          text: 'Pièces ajoutées avec succès !'
        }).then(() => {
          this.resetSelection();
  
          const modalElement = document.getElementById('addPiecesModal');
          if (modalElement) {
            const modal = bootstrap.Modal.getInstance(modalElement);
            modal?.hide();
          }
          this.loadInterventions();
  
          this.interventionService.getPiecesUsedForIntervention(this.selectedInterventionId).subscribe(data => {
            this.piecesUtilisees = data;
          });
        });
      },
      error: err => {
        console.error("Erreur lors de l'ajout :", err);
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: "Une erreur est survenue lors de l'ajout des pièces."
        });
      }
    });
  }
  

  resetSelection(): void {
    this.piecesSelectionnees = [];
    this.recherche = '';
    this.piecesDisponibles.forEach(p => {
      p.selected = false;
      p.quantite = 1;
    });
    this.piecesFiltrees = [...this.piecesDisponibles];
  }

  verifierQuantite(piece: any) {
    if (piece.quantite < 1) {
      piece.quantite = 1;
    }
  }

  openViewPiecesModal(intervention: any) {
    this.selectedInterventionId = intervention.intervention.id;
    this.resetSelection();
  
    // Appel à un service pour récupérer les pièces utilisées pour cette intervention
    this.interventionService.getPiecesUsedForIntervention(this.selectedInterventionId).subscribe(data => {
      this.piecesUtilisees = data;  // Mettre à jour les pièces utilisées
      console.log(data);
    });
  
    // Afficher la modal
    const modalElement = document.getElementById('viewPiecesModal');
    if (modalElement) {
      const modal = new window.bootstrap.Modal(modalElement);
      modal.show();
    }
  }

  removePiece(pieceId: number) {
    Swal.fire({
      title: 'Êtes-vous sûr ?',
      text: "Cette action supprimera la pièce de l'intervention.",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Oui, supprimer',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        this.interventionService.removePieceFromIntervention(this.selectedInterventionId, pieceId)
          .subscribe({
            next: () => {
              this.interventionService.getPiecesUsedForIntervention(this.selectedInterventionId).subscribe(data => {
                this.piecesUtilisees = data;
                Swal.fire(
                  'Supprimée !',
                  'La pièce a été supprimée avec succès.',
                  'success'
                );
              });
            },
            error: (err) => {
              console.error("Erreur lors de la suppression :", err);
              Swal.fire(
                'Erreur',
                "Impossible de supprimer la pièce.",
                'error'
              );
            }
          });
      }
    });
  }
  
  
  
}
