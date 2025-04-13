import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SousCategorie {
    id: number;
    nom: string;
    categorieId: number;
  }
  
  export interface Categorie {
    id: number;
    nom: string;
    subCategories: SousCategorie[];
  }
  
@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private apiUrl = 'http://localhost:9988/categories';

  constructor(private http: HttpClient) {}

  getCategories(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

 // Méthode pour récupérer les sous-catégories par ID de catégorie
 getSousCategories(categorieId: number): Observable<SousCategorie[]> {
  return this.http.get<SousCategorie[]>(`${this.apiUrl}/${categorieId}/subcategories`);
}
  
}
