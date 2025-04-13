import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReclamationService {
  private apiUrl = 'http://localhost:8888/reclamations';

  constructor(private http: HttpClient) {}


  assignReclamation(reclamationId: number, technicienId: number, responsableSAVId: number): Observable<any> {
    const params = new HttpParams()
      .set('technicienId', technicienId)
      .set('responsableSAVId', responsableSAVId);

    return this.http.put(`${this.apiUrl}/${reclamationId}/assign`, null, { params });
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
}
