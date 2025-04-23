import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReclamationService {
  private apiUrl = 'http://localhost:8888/reclamations';

  constructor(private http: HttpClient) {}


  assignReclamation(reclamationId: number, technicienId: number, responsableSAVId: number, deadline: string): Observable<string> {
    // Supprimer le 'Z' pour enlever le fuseau horaire
    const deadlineWithoutTimezone = new Date(deadline).toISOString().replace('Z', '');
    
    const params = new HttpParams()
      .set('technicienId', technicienId)
      .set('responsableSAVId', responsableSAVId)
      .set('deadline', deadlineWithoutTimezone);
  
    return this.http.put(`${this.apiUrl}/${reclamationId}/assign`, null, { params, responseType: 'text' });
  }
  

  
  ajouterReclamation(reclamation: any): Observable<any> {
    return this.http.post<any>(this.apiUrl+"/add", reclamation);
  }
  

  getReclamationsByClient(UserId: any ): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl+"/client/"+UserId);
  }

  annulerReclamation(reclamationId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${reclamationId}/annuler`, null);
  }

   // Méthode pour obtenir les réclamations par statut
   getReclamationsByStatut(statut: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/statut/${statut}`);
  }

   // Nouvelle méthode pour récupérer toutes les réclamations
   getAllReclamations(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/all`);
  }

  // methodes pour la dashboard :
  getReclamationsByPeriode(periode: string): Observable<{ [key: string]: number }> {
    return this.http.get<{ [key: string]: number }>(`${this.apiUrl}/par-${periode}`);
  }
  

  getReclamationsStatsParJour(): Observable<any> {
    return this.http.get(`${this.apiUrl}/stats-par-jour`);
  }

  getProduitsLesPlusReclames(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/produits-plus-reclames`);
  }
  
  

}
