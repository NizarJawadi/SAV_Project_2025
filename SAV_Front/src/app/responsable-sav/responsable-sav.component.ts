import { Component, OnInit } from '@angular/core';
import { ResponsableSAVServices } from '../services/ResponsableSAVServices';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';
import Swal from 'sweetalert2';
import { Modal } from 'bootstrap';

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


  addTechnicienForm = this.fb.group({
    username: [''],
    login: [''],
    email: [''],
    telephone: [''],
    matricule: [''],
    password: [''],
    regionResponsable: [''],
    priseEnFonction: ['']
  });

  

  formFields = [
    { name: 'username', label: "Nom d'utilisateur", type: 'text' },
    { name: 'login', label: 'Login', type: 'text' },
    { name: 'email', label: 'Email', type: 'email' },
    { name: 'telephone', label: 'Téléphone', type: 'text' },
    { name: 'matricule', label: 'Matricule', type: 'text' },
    { name: 'password', label: 'Password', type: 'text' },
    { name: 'regionResponsable', label: 'Région Responsable', type: 'text' },
    { name: 'priseEnFonction', label: 'Date d’embauche', type: 'date' }
  ];

  editTechnicienForm = this.fb.group({
    username: [''],
    login: [''],
    email: [''],
    telephone: [''],
    matricule: [''],
    regionResponsable: [''],
    priseEnFonction: ['']
  });

  technicienToEditId: number | null = null;
successMessage: string = '';
errorMessage: string = '';

  
  constructor(private responsableSAVService: ResponsableSAVServices,
              private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.loadTechniciens();
  }

  loadTechniciens(): void {
    this.responsableSAVService.getAllResponsable().subscribe((data: any[]) => {
      this.techniciens = data;
      console.log(this.techniciens)
    });
  }

  addTechnicien(): void {
    if (this.addTechnicienForm.invalid) return;
  
    this.responsableSAVService.addResponsableSAV(this.addTechnicienForm.value)
      .subscribe({
        next: () => {
          this.loadTechniciens();
          Swal.fire({
            icon: 'success',
            title: 'Succès',
            text: 'Technicien ajouté avec succès ✅',
            timer: 2000,
            showConfirmButton: false
          });
          this.addTechnicienForm.reset();
  
          const modalElement = document.getElementById('addTechnicienModal');
          if (modalElement) {
            const modal = Modal.getInstance(modalElement);
            modal?.hide();
          }
        },
        error: () => {
          Swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: 'Erreur lors de l\'ajout ❌',
          });
        }
      });

      const modalElement = document.getElementById('addTechnicienModal');
      if (modalElement) {
        const modal = Modal.getInstance(modalElement);
        modal?.hide();
      }
  }
  

  openEditModal(technicien: any): void {
    this.technicienToEditId = technicien.idUser;
    this.editTechnicienForm.patchValue({
      username: technicien.username,
      login: technicien.login,
      email: technicien.email,
      telephone: technicien.telephone,
      matricule: technicien.matricule,
      regionResponsable: technicien.regionResponsable,
      priseEnFonction: technicien.priseEnFonction?.substring(0, 10)
    });
  }
  
  updateTechnicien(): void {
    if (this.editTechnicienForm.invalid || this.technicienToEditId === null) return;
  
    this.responsableSAVService.updateResponsableSAV(this.technicienToEditId, this.editTechnicienForm.value)
      .subscribe({
        next: () => {
          this.loadTechniciens();
          Swal.fire({
            icon: 'success',
            title: 'Succès',
            text: 'Technicien modifié avec succès ✅',
            timer: 2000,
            showConfirmButton: false
          });
        },
        error: () => {
          Swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: 'Erreur lors de la modification ❌',
        })
      }
      });

      const modalElement = document.getElementById('editTechnicienModal');
if (modalElement) {
  const modal = Modal.getInstance(modalElement);
  modal?.hide();
}

  }
  

  onDelete(id: number): void {
    Swal.fire({
      title: 'Êtes-vous sûr ?',
      text: 'Cette action est irréversible.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Oui, supprimer !',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        this.responsableSAVService.deleteResponsableSAV(id).subscribe({
          next: () => {
            this.loadTechniciens();
            Swal.fire({
              icon: 'success',
              title: 'Supprimé',
              text: 'Technicien supprimé avec succès ✅',
              timer: 2000,
              showConfirmButton: false
            });
          },
          error: () => {
            Swal.fire({
              icon: 'error',
              title: 'Erreur',
              text: 'Erreur lors de la suppression ❌',
            });
          }
        });
      }
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


}