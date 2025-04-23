import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from '../header/header.component';
import { ProduitService } from '../services/ProduitService';
import { Categorie, CategoryService, SousCategorie } from '../services/CategoryService';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-product',
  standalone: true,
  imports: [CommonModule, FormsModule ,ReactiveFormsModule, HeaderComponent],
  templateUrl: './produit.component.html',
  styleUrls: ['./produit.component.scss'],
})
export class ProduitComponent implements OnInit {
  products: any[] = [];
  selectedProduct: any = null;
  productForm: FormGroup; // Formulaire pour le produit // Reactive form for adding/updating a product
  Math = Math;
  currentPage: number = 0;
  pageSize: number = 10;
  isEditMode: boolean = false; // To toggle between add and edit mode
  isLoading: boolean = false;  // Variable pour gérer le chargement
  
  categories: Categorie[] = [];
  sousCategories: SousCategorie[] = [];
    
  selectedCategoryId: number = 0;
  selectedSubCategoryId: number = 0;
 selectedFile: any ;
 imagePreview:any ;
  constructor(private productService: ProduitService,
              private fb: FormBuilder,
              private categoryService: CategoryService,
            ) {
    // Formulaire pour ajouter ou modifier un produit
    this.productForm = this.fb.group({
      nom: ['', Validators.required],
      description: ['', Validators.required],
      reference: ['', Validators.required],
      //dateAchat: ['', Validators.required],
      //garantieExpireLe: ['', Validators.required],
      dureeGarantie: [0, Validators.required],
      statut: ['Disponible'],
      prix: [0, [Validators.required, Validators.min(0)]],
      categorieId: [{ value: 0, disabled: false }, Validators.required],
      subCategoryId: [{ value: 0, disabled: true }, Validators.required],
      imageUrl: ['', Validators.required]
    });

    this.productForm.get('categorieId')?.valueChanges.subscribe((categoryId) => {
      if (categoryId) {
        this.productForm.get('subCategoryId')?.enable(); // Enable subCategoryId if a category is selected
      } else {
        this.productForm.get('subCategoryId')?.disable(); // Disable if no category selected
      }
    });
  
  }

  ngOnInit(): void {
    this.loadProducts();
    this.loadCategories();
  }


  loadCategories(): void {
    this.categoryService.getCategories().subscribe((categories) => {
      this.categories = categories;
    });
  }

 /* onCategoryChange(categoryId: number): void {
    this.selectedCategoryId = categoryId;
    this.categoryService.getSousCategories(categoryId).subscribe((subCategories) => {
      this.sousCategories = subCategories;
    });
  }*/

  onCategoryChange(event: Event): void {
    const target = event.target as HTMLSelectElement; // or HTMLInputElement depending on your element type
    const value = target.value;
    this.categoryService.getSousCategories(parseInt(value)).subscribe((subCategories) => {
      this.sousCategories = subCategories;
    });
  }

  


  onSubmit(): void {
    console.log(this.productForm.valid)
    console.log(this.productForm.value)
    if (this.productForm.valid) {
      const productData = this.productForm.value;
  
      // Création d'un objet FormData pour envoyer les données et le fichier
      const formData = new FormData();
      
      // Ajouter les autres champs du formulaire
      formData.append('nom', productData.nom);
      formData.append('description', productData.description);
      formData.append('reference', productData.reference);
      //formData.append('dateAchat', productData.dateAchat);
      //formData.append('garantieExpireLe', productData.garantieExpireLe);
      formData.append('dureeGarantie', productData.dureeGarantie.toString());
      formData.append('statut', productData.statut);
      formData.append('prix', productData.prix);
      formData.append('categorieId', productData.categorieId.toString());
      formData.append('subCategoryId', productData.subCategoryId.toString());
  
      // Ajouter le fichier à FormData
      if (this.selectedFile) {
        formData.append('imageUrl', this.selectedFile, this.selectedFile.name);
      }
  
      console.log('FormData:', formData); // Vérifiez ce que contient FormData
      this.reloadPage();
      // Soumettre le FormData à l'API
      if (this.isEditMode) {
        this.productService.updateProduct(this.selectedProduct.id, formData).subscribe({
          next: (response) => {
            Swal.fire('Mis à jour', 'Le produit a été mis à jour avec succès.', 'success');
            this.resetForm();
            this.loadProducts();
          },
          error: () => Swal.fire('Erreur', 'Erreur lors de la mise à jour du produit.', 'error')
        });
      } else {
        this.productService.addProduit(formData).subscribe({
          next: (response) => {
            Swal.fire('Ajouté', 'Le produit a été ajouté avec succès.', 'success');
            this.resetForm();
            this.loadProducts();
          },
          error: () => Swal.fire('Erreur', 'Erreur lors de l\'ajout du produit.', 'error')
        });
      }
      
    }

    
  }
  
  
  reloadPage(): void {
    window.location.reload(); // Reload the page after form submission
  }

  onFileChange(event: any): void {
    const file = event.target.files[0];
    this.selectedFile = file;
    console.log(file)
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imagePreview = e.target.result; // Stocke l'URL de prévisualisation
      };
      reader.readAsDataURL(file);
   

      // Mettre à jour le champ 'imageUrl' dans le formulaire
      this.productForm.patchValue({
        imageUrl: file.name  // Mettre à jour avec le nom du fichier
      });
    }
  }
  

  onImageChange(event: any): void {
  const file = event.target.files[0];
  if (file) {
    // Récupérer uniquement le nom du fichier
    const fileName = file.name;

    // Mettre à jour le formulaire avec uniquement le nom du fichier
    this.productForm.patchValue({
      imageUrl: fileName
    });
  }
}

  
  

  loadProducts(): void {
    this.isLoading = true;  // Indique que les produits sont en cours de chargement
    this.productService.getAllProducts().subscribe({
      next: (data: any[]) => {
        this.products = data;
        console.log(this.products)
        this.isLoading = false;  // Stoppe l'indicateur de chargement
      },
      error: (err: any) => {
        console.error('Erreur lors du chargement des produits:', err);
        this.isLoading = false;  // Stoppe l'indicateur de chargement
      },
    });
  }

  onDelete(id: number): void {
    Swal.fire({
      title: 'Êtes-vous sûr ?',
      text: 'Cette action est irréversible !',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Oui, supprimer',
      cancelButtonText: 'Annuler',
    }).then((result) => {
      if (result.isConfirmed) {
        this.productService.deleteProduct(id).subscribe({
          next: () => {
            this.products = this.products.filter((product) => product.id !== id);
            this.loadProducts();
            Swal.fire('Supprimé !', 'Le produit a été supprimé.', 'success');
          },
          error: (err: any) => {
            console.error('Erreur lors de la suppression du produit:', err);
            Swal.fire('Erreur', 'Une erreur est survenue.', 'error');
          },
        });
      }
    });
  }
  

  onEdit(product: any): void {
    this.selectedProduct = product;
    this.productForm.patchValue({
      nom: product.nom,
      description: product.description,
      prix: product.prix,
      reference: product.reference,
      dateAchat: product.dateAchat,
      garantieExpireLe: product.garantieExpireLe,
      statut: product.statut
    });
    this.isEditMode = true;
  }

 /* onSubmit(): void {
    if (this.productForm.valid) {
      const updatedProduct = { ...this.selectedProduct, ...this.productForm.value };
      this.productService.updateProduct(this.selectedProduct.id, updatedProduct).subscribe({
        next: () => {
          const index = this.products.findIndex((p) => p.id === this.selectedProduct.id);
          this.products[index] = updatedProduct;
          this.selectedProduct = null;
          alert('Produit mis à jour avec succès.');
        },
        error: (err: any) => console.error('Erreur lors de la mise à jour du produit:', err),
      });
    }
  }*/

  openProductDetails(product: any): void {
    this.selectedProduct = product;
  }

  onCancelEdit(): void {
    Swal.fire({
      title: 'Annuler la modification ?',
      text: 'Les modifications en cours seront perdues.',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Oui',
      cancelButtonText: 'Non',
    }).then((result) => {
      if (result.isConfirmed) {
        this.selectedProduct = null;
        this.isEditMode = false;
        this.resetForm();
      }
    });
  }
  

  // Fonction pour aller à la page suivante
  nextPage(): void {
    if ((this.currentPage + 1) * this.pageSize < this.products.length) {
      this.currentPage++;
    }
  }

  // Fonction pour aller à la page précédente
  prevPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
    }
  }

  resetForm(): void {
    this.productForm.reset();
    this.isEditMode = false;
  }
}
