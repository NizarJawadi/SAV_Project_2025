import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InterventionService {
  private apiUrl = 'http://localhost:8888/interventions';

  constructor(private http: HttpClient) {}

  getAllInterventions(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  getInterventionById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  getInterventionsByUserId(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/by-technicien/id/${id}`);
  }

   // Méthode pour mettre à jour le statut d'une intervention
   updateInterventionStatus(id: number, status: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}/status`, { status });
  }

  addPiece(id: number, pieceId: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${id}/pieces/${pieceId}`, {});
  }

  removePiece(id: number, pieceId: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}/pieces/${pieceId}`);
  }

  cloturerIntervention(id: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}/cloturer`, {});
  }


  getAllPieces() {
    return this.http.get<any[]>('http://localhost:8082/pieces'); // adresse du microservice pièces
  }

 

  getPiecesUsedForIntervention(interventionId: number) {
    return this.http.get<any[]>(`${this.apiUrl}/${interventionId}/pieces`);
  }
  
  
  
  ajouterPieces(interventionId: number, pieces: { pieceId: number, quantite: number }[]) {
    return this.http.post(`${this.apiUrl}/${interventionId}/ajouter-pieces`, pieces);
  }
  

  removePieceFromIntervention(interventionId: number, pieceId: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${interventionId}/pieces/${pieceId}`);
  }
  
  
  //methode pour dashboard 
  getStats(): Observable<any> {
    return this.http.get(this.apiUrl+"/stats/interventions");
  }
}
