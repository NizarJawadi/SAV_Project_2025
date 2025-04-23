import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { HeaderComponent } from '../header/header.component';
import { PieceDeRechangeService } from '../services/PieceDeRechangeService';
import * as bootstrap from 'bootstrap';
import Swal from 'sweetalert2';



@Component({
  selector: 'app-piece-de-rechange',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, HeaderComponent],
  templateUrl: './piece-de-rechange.component.html',
  styleUrls: ['./piece-de-rechange.component.css']
})
export class PieceDeRechangeComponent implements OnInit {
  pieces: any[] = [];
  pieceForm: FormGroup;
  selectedPiece: any | null = null;
  isEditMode = false;
  isLoading = false;
  
  // Pagination
  currentPage = 0;
  pageSize = 10;

  imagePreview: string | ArrayBuffer | null = null;

  constructor(private fb: FormBuilder, private pieceService: PieceDeRechangeService) {
    this.pieceForm = this.fb.group({
      nom: ['', Validators.required],
      reference: ['', Validators.required],
      prix: ['', [Validators.required, Validators.min(0)]],
      quantiteStock: ['', [Validators.required, Validators.min(0)]],
     
    });
  }


  openAddModal() {
    this.isEditMode = false;
    this.selectedPiece = null;
    this.pieceForm.reset();
    const modal = document.getElementById('pieceModal');
    if (modal) {
      const bsModal = new bootstrap.Modal(modal);
      bsModal.show();
    }
  }
  
  ngOnInit(): void {
    this.fetchPieces();
  }

  fetchPieces(): void {
    this.isLoading = true;
    this.pieceService.getAllPieces().subscribe({
      next: (data: any[]) => {
        this.pieces = data;
        this.isLoading = false;
      },
      error: (error: any) => {
        Swal.fire(
          'Erreur',
          'Impossible de charger les pièces.',
          'error'
        );
        this.isLoading = false;
      }
    });
  }
  

  onSubmit() {
    if (this.pieceForm.invalid) return;
  
    const pieceData = this.pieceForm.value;
  
    if (this.isEditMode && this.selectedPiece) {
      this.pieceService.updatePiece(this.selectedPiece.id, pieceData).subscribe(() => {
        Swal.fire(
          'Modifié !',
          'La pièce a été modifiée avec succès.',
          'success'
        ).then(() => {
          this.closeModal() ;
        });
        this.fetchPieces();
        this.resetForm();
      }, (error) => {
        Swal.fire(
          'Erreur',
          'Une erreur est survenue lors de la modification de la pièce.',
          'error'
        );
      });
    } else {
      this.pieceService.addPiece(pieceData).subscribe(() => {
        Swal.fire(
          'Ajouté !',
          'La pièce a été ajoutée avec succès.',
          'success'
        ).then(() => {
          this.closeModal() ;
        });
        this.fetchPieces();
        this.resetForm();
      }, (error) => {
        Swal.fire(
          'Erreur',
          'Une erreur est survenue lors de l\'ajout de la pièce.',
          'error'
        );
      });
    }

    this.closeModal() ;
  }
  

  
  

  onEdit(piece: any) {
    this.isEditMode = true;
    this.selectedPiece = piece;
    this.pieceForm.patchValue(piece);
    
    const modal = document.getElementById('pieceModal');
    if (modal) {
      const bsModal = new bootstrap.Modal(modal);
      bsModal.show();
    }
  }
  

  onDelete(id: number) {
    Swal.fire({
      title: 'Êtes-vous sûr ?',
      text: 'Vous ne pourrez pas récupérer cette pièce après suppression !',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Oui, supprimer !',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        this.pieceService.deletePiece(id).subscribe(() => {
          Swal.fire(
            'Supprimé !',
            'La pièce a été supprimée avec succès.',
            'success'
          );
          this.fetchPieces();  // Recharger les données après suppression
        });
      }
    });
  }
  

  openPieceDetails(piece: any) {
    this.selectedPiece = piece;
    const modal = document.getElementById('detailsModal');
    if (modal) {
      const bsModal = new bootstrap.Modal(modal);
      bsModal.show();
    }
  }

  closeModal() {
    const modalElement = document.getElementById('pieceModal');
    if (modalElement) {
      const modalInstance = bootstrap.Modal.getInstance(modalElement);
      if (modalInstance) {
        modalInstance.hide(); // Ferme la modal existante
      }
    }
  }
  

  resetForm() {
    this.pieceForm.reset();
    this.isEditMode = false;
    this.selectedPiece = null;
    this.imagePreview = null;
    this.closeModal();
  }

  prevPage() {
    if (this.currentPage > 0) this.currentPage--;
  }

  nextPage() {
    if ((this.currentPage + 1) * this.pageSize < this.pieces.length) this.currentPage++;
  }

 
}
