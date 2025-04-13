import { Component, OnInit } from '@angular/core';
import { ResponsableSAVServices } from '../services/ResponsableSAVServices';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';

@Component({
  selector: 'app-responsable-sav',
  standalone: true,
  imports: [HeaderComponent , CommonModule , ReactiveFormsModule , FormsModule ],
  templateUrl: './responsable-sav.component.html',
  styleUrl: './responsable-sav.component.css'
})
export class ResponsableSAVComponent implements OnInit {
  techniciens: any[] = [];
  selectedTechnicien: any;
  Math =Math ;
  currentPage: number = 0;
  pageSize: number = 5;

  constructor(private responsableSAVService: ResponsableSAVServices) {}

  ngOnInit(): void {
    this.loadTechniciens();
  }

  loadTechniciens(): void {
    this.responsableSAVService.getAllResponsable().subscribe((data: any[]) => {
      this.techniciens = data;
      console.log(this.techniciens)
    });
  }

  openTechnicienDetails(technicien: any): void {
    this.selectedTechnicien = technicien;
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

  onDelete(id: number): void {
    // Ici, vous pouvez ajouter la logique pour supprimer un technicien
    console.log(`Technicien avec l'ID ${id} supprimé`);
  }
}