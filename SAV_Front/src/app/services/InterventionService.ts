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
}
