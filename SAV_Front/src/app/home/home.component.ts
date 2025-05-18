import { Component, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ProduitService } from '../services/ProduitService';
import { CategoryService } from '../services/CategoryService';
import { FooterComponent } from '../ClientCompnent/components/footer/footer.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink , FooterComponent],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent {
  private lastScrollTop: number = 0;
  public searchTop: number = 190;
  searchTerm: string = '';
  minPrice: number | null = null;
  maxPrice: number | null = null;
  selectedCategory: string = '';
  selectedSubCategory: string = ''; // Ajout du filtre par sous-catégorie
  selectedBrand: string = '';
  selectedAvailability: string = '';
  selectedRating: number | null = null;
  sortBy: string = '';
  produits: any[] = [];
  categories: any[] = [];
  selectedSubCategoryId: number | null = null;


   // Modal state
   isModalOpen: boolean = false;
   selectedProduct: any;
 
  constructor(
    private produitService: ProduitService,
    private categoryService: CategoryService
  ) {
    //this.loadFilters();
  }

  ngOnInit(): void {
    // Charger les catégories avec sous-catégories
    this.categoryService.getCategories().subscribe((data) => {
      this.categories = data.map((category: any) => ({
        ...category,
        isActive: false,
      }));

      // Charger les produits après les catégories
      this.produitService.getProduits().subscribe((data) => {
        this.produits = data;
        console.log(this.produits)
      });
    });
  }


     // Déplacer cette méthode avant ngOnInit pour plus de clarté
  getShortDescription(description: string): string {
    if (!description) return '';
    return description.length > 50 ? description.substring(0, 50) + '...' : description;
  }

toggleDescription(produit: any): void {
  produit.showFullDescription = !produit.showFullDescription;
  console.log(produit.nom + ' => showFullDescription:', produit.showFullDescription);
}


  toggleSubcategories(category: any): void {
    category.isActive = !category.isActive;
  }

  selectSubCategory(subcategory: any): void {
    this.selectedSubCategory = subcategory.nom;
    this.selectedSubCategoryId = subcategory.id;
    this.loadProduits();
  }

  @HostListener('window:scroll', [])
  onScroll(): void {
    let currentScroll = window.pageYOffset || document.documentElement.scrollTop;
    this.searchTop =
      currentScroll > this.lastScrollTop
        ? Math.max(this.searchTop - 5, 50)
        : Math.min(this.searchTop + 5, 190);
    this.lastScrollTop = currentScroll;
  }

  loadProduits() {
    if (this.selectedSubCategoryId) {
      this.produitService.getProduitsBySubCategorie(this.selectedSubCategoryId).subscribe((data) => {
        this.produits = data;
      });
    } else {
      this.produitService.getProduits().subscribe((data) => {
        this.produits = data;
      });
    }
  }

  filteredProducts() {
    this.saveFilters();
    return this.produits
      .filter(
        (product) =>
          (this.searchTerm === '' ||
            product.nom.toLowerCase().includes(this.searchTerm.toLowerCase())) &&
          (this.minPrice === null || product.prix >= this.minPrice) &&
          (this.maxPrice === null || product.prix <= this.maxPrice) &&
          (this.selectedCategory === '' || product.categorie === this.selectedCategory) &&
          (this.selectedSubCategory === '' || product.sousCategorie === this.selectedSubCategory) &&
          (this.selectedBrand === '' || product.marque === this.selectedBrand) &&
          (this.selectedAvailability === '' ||
            (product.disponibilite && product.disponibilite.toString() === this.selectedAvailability)) &&
          (this.selectedRating === null || product.rating >= this.selectedRating)
      )
      .sort((a, b) => {
        switch (this.sortBy) {
          case 'priceAsc':
            return a.prix - b.prix;
          case 'priceDesc':
            return b.prix - a.prix;
          case 'rating':
            return b.rating - a.rating;
          default:
            return 0;
        }
      });
  }

  saveFilters() {
    const filters = {
      searchTerm: this.searchTerm,
      minPrice: this.minPrice,
      maxPrice: this.maxPrice,
      selectedCategory: this.selectedCategory,
      selectedSubCategory: this.selectedSubCategory,
      selectedBrand: this.selectedBrand,
      selectedAvailability: this.selectedAvailability,
      selectedRating: this.selectedRating,
      sortBy: this.sortBy,
    };
    localStorage.setItem('productFilters', JSON.stringify(filters));
  }

  resetFilters() {
    localStorage.removeItem('productFilters');
    this.searchTerm = '';
    this.minPrice = null;
    this.maxPrice = null;
    this.selectedCategory = '';
    this.selectedSubCategory = ''; // Réinitialisation de la sous-catégorie
    this.selectedBrand = '';
    this.selectedAvailability = '';
    this.selectedRating = null;
    this.sortBy = '';
  }

  // Toggle modal visibility
  openModal(produit: any): void {
    this.selectedProduct = produit;  // Save the selected product
    this.isModalOpen = true; // Open the modal
  }

  closeModal(): void {
    this.isModalOpen = false;  // Close the modal
  }

  // Handle purchase confirmation
  confirmPurchase(): void {
    alert('Achat confirmé pour ' + this.selectedProduct.nom);
    this.closeModal(); // Close the modal after confirmation
  }


  
}
