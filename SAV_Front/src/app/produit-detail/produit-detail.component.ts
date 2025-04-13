import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProduitService } from '../services/ProduitService';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-produit-detail',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule ,FormsModule],
  templateUrl: './produit-detail.component.html',
  styleUrl: './produit-detail.component.css'
})
export class ProduitDetailComponent {
  produit: any;
  quantite: number = 1;

  constructor(
    private route: ActivatedRoute,
    private produitService: ProduitService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.produitService.getProduitById(id).subscribe((data) => {
        this.produit = data;
      });
    }
  }

  acheterProduit(): void {
    alert(`Achat de ${this.quantite} unité(s) du produit ${this.produit.nom}`);
  }
}
