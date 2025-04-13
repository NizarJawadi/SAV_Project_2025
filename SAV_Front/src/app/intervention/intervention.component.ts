import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { InterventionService } from '../services/InterventionService';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HeaderComponent } from '../header/header.component';
import { CdkDragDrop, DragDropModule, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-intervention',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, HeaderComponent, DragDropModule],
  templateUrl: './intervention.component.html',
  styleUrls: ['./intervention.component.css']
})
export class InterventionComponent implements OnInit {
  interventions: any[] = [];
  enAttente: any[] = [];
  enCours: any[] = [];
  terminees: any[] = [];
  showGroupedView = false;

  constructor(private interventionService: InterventionService, private router: Router, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.fetchInterventions();
  }

  fetchInterventions(): void {
    this.interventionService.getAllInterventions().subscribe(data => {
        if (data && Array.isArray(data)) {
            this.interventions = data;
            console.log(data)
            this.filterInterventions();
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

  toggleView(): void {
    this.showGroupedView = !this.showGroupedView;
  }

  viewDetails(intervention: any): void {
    const modalElement = document.getElementById('interventionModal');
    if (modalElement) {
      const modal = new window.bootstrap.Modal(modalElement);
      modal.show();
    } else {
      console.error('Modal element not found');
    }
  }

  // Gestion correcte du Drag & Drop
  onDrop(event: CdkDragDrop<any[]>, listName: string): void {
    console.log('Données de l\'élément:', event.item);
    console.log('Données de l\'élément cdkDragData:', event.item.data);
  
    // Vérifier que event.item.data est bien défini
    if (!event.item.data) {
      console.error('Erreur: event.item.data est indéfini.');
      return;
    }
  
    const movedData = event.item.data; // L'objet complet contenant intervention, client, reclamation
  
    // Vérifier que l'objet contient bien l'intervention
    if (!movedData.intervention) {
      console.error('Erreur: l\'objet ne contient pas de propriété intervention:', movedData);
      return;
    }
  
    const movedIntervention = movedData.intervention; // Extraire intervention
  
    // Mise à jour du statut
    movedIntervention.statut = this.getStatusFromList(listName);
  
    this.interventionService.updateInterventionStatus(
      movedIntervention.id,
      movedIntervention.statut
    ).subscribe(
      () => console.log('Intervention mise à jour avec succès:', movedIntervention),
      (error) => console.error('Erreur de mise à jour:', error)
    );
  
    // Gérer le déplacement de l'élément
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
    }
  }
  
  

  getStatusFromList(listName: string): string {
    switch (listName) {
      case 'enAttente': return 'EN_ATTENTE';
      case 'enCours': return 'EN_COURS';
      case 'terminees': return 'TERMINEE';
      default: return 'INCONNU';
    }
  }
}
