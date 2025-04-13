import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { param } from 'jquery';

@Injectable({
  providedIn: 'root'
})
export class ProduitService {
  private apiUrl = 'http://localhost:8888/produits';

  constructor(private http: HttpClient) {}

  getProduits(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  getProduitById(id :any): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${id}`);
  }

  getProduitsBySubCategorie(subCategorieId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/subcategorie/${subCategorieId}`);
  }

  addProduit(formData: FormData): Observable<any> {
    return this.http.post<any>(this.apiUrl, formData);
  }
  
 // Autres méthodes pour la gestion des produits (update, delete, etc.)
 updateProduct(id: number, productData: FormData): Observable<any> {
  return this.http.put<any>(`${this.apiUrl}/${id}`, productData);
}
  getAllProducts(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /*updateProduct(id: number, product: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, product);
  }*/
}
