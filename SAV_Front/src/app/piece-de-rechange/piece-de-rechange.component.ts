import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { HeaderComponent } from '../header/header.component';
import { PieceDeRechangeService } from '../services/PieceDeRechangeService';
import * as bootstrap from 'bootstrap';



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
    this.pieceService.getAllPieces().subscribe({
      next: (data: any[]) => this.pieces = data,
      error: (error: any) => console.error('Erreur lors du chargement des pièces', error)
    });
  }

  onSubmit() {
    if (this.pieceForm.invalid) return;

    const pieceData = this.pieceForm.value;
    
    if (this.isEditMode && this.selectedPiece) {
      this.pieceService.updatePiece(this.selectedPiece.id, pieceData).subscribe(() => {
        this.fetchPieces();
        this.resetForm();
      });
    } else {
      this.pieceService.addPiece(pieceData).subscribe(() => {
        this.fetchPieces();
        this.resetForm();
      });
    }
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
    if (confirm('Voulez-vous vraiment supprimer cette pièce ?')) {
      this.pieceService.deletePiece(id).subscribe(() => this.fetchPieces());
    }
  }

  openPieceDetails(piece: any) {
    this.selectedPiece = piece;
    const modal = document.getElementById('detailsModal');
    if (modal) {
      const bsModal = new bootstrap.Modal(modal);
      bsModal.show();
    }
  }
  

  resetForm() {
    this.pieceForm.reset();
    this.isEditMode = false;
    this.selectedPiece = null;
    this.imagePreview = null;
  }

  prevPage() {
    if (this.currentPage > 0) this.currentPage--;
  }

  nextPage() {
    if ((this.currentPage + 1) * this.pageSize < this.pieces.length) this.currentPage++;
  }

 
}
