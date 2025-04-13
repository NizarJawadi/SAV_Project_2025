import { Component, OnInit } from '@angular/core';
import { TechnicienService } from '../services/TechnicienServices';
import { HeaderComponent } from '../header/header.component';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-technicien',
  standalone: true,
  imports: [HeaderComponent , CommonModule , ReactiveFormsModule , FormsModule ],
  templateUrl: './technicien.component.html',
  styleUrl: './technicien.component.css'
})
export class TechnicienComponent implements OnInit {
  techniciens: any[] = [];
  currentPage: number = 0;
  pageSize: number = 10;
  isLoading: boolean = true;
  selectedTechnicien: any = null;
  Math = Math;

  
  constructor(private technicienService: TechnicienService) {}

  ngOnInit(): void {
    this.fetchTechniciens();
  }

  fetchTechniciens(): void {
    this.technicienService.getAllTechniciens().subscribe(
      (data) => {
        this.techniciens = data;
        console.log(this.techniciens)
        this.isLoading = false;
      },
      (error) => {
        console.error('Erreur lors de la récupération des techniciens', error);
        this.isLoading = false;
      }
    );
  }

  prevPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
    }
  }

  nextPage(): void {
    if ((this.currentPage + 1) * this.pageSize < this.techniciens.length) {
      this.currentPage++;
    }
  }

  openTechnicienDetails(technicien: any): void {
    this.selectedTechnicien = technicien;
  }

  onDelete(id: number): void {
    if (confirm('Voulez-vous vraiment supprimer ce technicien ?')) {
      // Ajoute ici l’appel à l’API pour supprimer le technicien
      console.log(`Technicien supprimé : ${id}`);
    }
  }
}