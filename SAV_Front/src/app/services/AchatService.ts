import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AchatService {
  private apiUrl = 'http://localhost:8888/historique'; // Assure-toi que cette URL est correcte

  constructor(private http: HttpClient) {}

  ajouterAchat(achat: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/ajouter`, achat);
  }

 /* ajouterAchat(clientId: number, produitId: number, quantite: number, prixUnitaire: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/ajouter/ajouter`, null, {
      params: {
        clientId: clientId.toString(),
        produitId: produitId.toString(),
        quantite: quantite.toString(),
        prixUnitaire: prixUnitaire.toString()
      }
    });
  }
    */
  
}
