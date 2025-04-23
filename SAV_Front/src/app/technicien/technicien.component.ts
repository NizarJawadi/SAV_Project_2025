import { Component, OnInit } from '@angular/core';
import { TechnicienService } from '../services/TechnicienServices';
import { HeaderComponent } from '../header/header.component';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import * as bootstrap from 'bootstrap';
import Swal from 'sweetalert2';


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


  selectedTechnicienForEdit: any = null;
  addTechnicienForm!: FormGroup;

  formFields = [
    { name: 'username', label: "Nom d'utilisateur", type: 'text' },
    { name: 'login', label: 'Login', type: 'text' },
    { name: 'password', label: 'Mot de passe', type: 'password' },
    { name: 'email', label: 'Email', type: 'email' },
    { name: 'specialite', label: 'Spécialité', type: 'text' },
    { name: 'adresse', label: 'Adresse', type: 'text' },
    { name: 'telephone', label: 'Téléphone', type: 'text' },
    { name: 'tarifParHeure', label: 'Tarif par heure', type: 'number' },
    { name: 'dateEumbauche', label: "Date d'embauche", type: 'date' },
    { name: 'matricule', label: 'Matricule', type: 'text' },
    { name: 'disponibilite', label: 'Disponibilité', type: 'text' }
  ];

  editTechnicienForm = this.fb.group({
    username: ['', Validators.required],
    login: ['', Validators.required],
    password: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    specialite: ['', Validators.required],
    adresse: ['', Validators.required],
    telephone: ['', Validators.required],
    tarifParHeure: [0, Validators.required],
    dateEumbauche: ['', Validators.required],
    matricule: ['', Validators.required],
    disponibilite: ['Disponible', Validators.required]
  });
  

  
  constructor(private technicienService: TechnicienService,
              private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.fetchTechniciens();
    this.addTechnicienForm = this.fb.group({
      username: ['', Validators.required],
      login: ['', Validators.required],
      password: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      specialite: ['', Validators.required],
      adresse: ['', Validators.required],
      telephone: ['', Validators.required],
      tarifParHeure: [0, Validators.required],
      dateEumbauche: ['', Validators.required],
      matricule: ['', Validators.required],
      disponibilite: ['Disponible', Validators.required]
    });
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


  onAddTechnicien(): void {
    if (this.addTechnicienForm.valid) {
      const technicienData = {
        ...this.addTechnicienForm.value,
        role: 'TECHNICIEN',
        dateEumbauche: this.addTechnicienForm.value.dateEumbauche + 'T01:00:00'
      };
  
      this.technicienService.addTechnicien(technicienData).subscribe(
        (response: any) => {
          this.fetchTechniciens(); // rafraîchir la liste
          this.addTechnicienForm.reset();
  
          // Fermer la modale manuellement
          const modalEl = document.getElementById('addTechnicienModal');
          const modal = bootstrap.Modal.getInstance(modalEl!);
          modal?.hide();
  
          // ✅ Afficher une alerte personnalisée
          Swal.fire({
            icon: 'success',
            title: 'Technicien ajouté !',
            text: 'Le technicien a été ajouté avec succès.',
            timer: 2000,
            showConfirmButton: false
          });
        },
        (error: any) => {
          console.error("Erreur lors de l'ajout du technicien :", error);
  
          Swal.fire({
            icon: 'error',
            title: 'Erreur !',
            text: 'Impossible d\'ajouter le technicien. Veuillez réessayer.',
            confirmButtonColor: '#d33'
          });
        }
      );
    } else {
      Swal.fire({
        icon: 'warning',
        title: 'Formulaire invalide',
        text: 'Veuillez remplir tous les champs requis.',
        confirmButtonColor: '#3085d6'
      });
    }
  }

  openEditModal(technicien: any): void {
    this.selectedTechnicienForEdit = technicien;
    this.editTechnicienForm.patchValue({
      ...technicien,
      dateEumbauche: technicien.dateEmbauche?.split('T')[0]
    });
  }


  onUpdateTechnicien(): void {
    if (this.editTechnicienForm.valid && this.selectedTechnicienForEdit) {
      const updatedData = {
        ...this.editTechnicienForm.value,
        idUser: this.selectedTechnicienForEdit.idUser,
        dateEumbauche: this.editTechnicienForm.value.dateEumbauche + 'T01:00:00'
      };
  
      this.technicienService.updateTechnicien(updatedData).subscribe(
        (res: any) => {
          this.fetchTechniciens();
          const modalEl = document.getElementById('editTechnicienModal');
          const modal = bootstrap.Modal.getInstance(modalEl!);
          modal?.hide();
          Swal.fire({
            icon: 'success',
            title: 'Technicien modifié avec succès !',
            timer: 2000,
            showConfirmButton: false
          });
        },
        (error: any) => {
          console.error('Erreur lors de la mise à jour du technicien :', error);
          Swal.fire({
            icon: 'error',
            title: 'Erreur !',
            text: 'Impossible de modifier le technicien.',
            confirmButtonColor: '#d33'
          });
        }
      );
    }
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
    Swal.fire({
      title: 'Êtes-vous sûr ?',
      text: 'Cette action est irréversible !',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Oui, supprimer',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        this.technicienService.deleteTechnicien(id).subscribe(
          () => {
            this.techniciens = this.techniciens.filter(t => t.idUser !== id);
            Swal.fire({
              icon: 'success',
              title: 'Technicien ajouté !',
              showConfirmButton: false,
              timer: 1500
            });
            
          },
          (error) => {
            console.error('Erreur lors de la suppression du technicien', error);
            Swal.fire(
              'Erreur !',
              'Une erreur est survenue lors de la suppression.',
              'error'
            );
          }
        );
      }
    });
  }
  
  
}