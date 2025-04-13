import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ReclamationService } from '../services/reclamationService';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';
import * as bootstrap from 'bootstrap';
import { TechnicienService } from '../services/TechnicienServices';

@Component({
  selector: 'app-reclamation',
  standalone: true,
  imports: [HeaderComponent, CommonModule, ReactiveFormsModule, FormsModule],
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


  searchNumeroSerie: string = '';


  techniciens: any[] = []; 
  specialites: string[] = []; // Assurez-vous qu'il est initialisé
  selectedSpecialite: string | null = null;
  selectedTechnicienId: number | null = null;
  currentReclamationId: any;
  responsableSAVId:any ;

  constructor(private reclamationService: ReclamationService,
    private technicienService: TechnicienService,
    private cdRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadReclamationsByStatut(this.statutFilter);
    this.loadSpecialites();
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
   
    if (this.currentReclamationId && this.selectedTechnicienId) {
      
      console.log(this.selectedTechnicienId)
      console.log(this.currentReclamationId)
      this.responsableSAVId = Number(localStorage.getItem("UserId")); 
      this.currentReclamationId = Number(this.currentReclamationId); 
      this.selectedTechnicienId = Number(this.selectedTechnicienId);
      this.reclamationService.assignReclamation(this.currentReclamationId, this.selectedTechnicienId, this.responsableSAVId)
        .subscribe(() => {
          alert("Technicien assigné avec succès !");
          this.getAllReclamations();
          document.getElementById('assignModal')?.click();
        });
    }
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
}
