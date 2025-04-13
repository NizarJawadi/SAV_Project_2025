import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PieceDeRechangeService {
  private apiUrl = 'http://localhost:8888/pieces'; // URL de l'API Spring Boot

  constructor(private http: HttpClient) {}

  // Récupérer toutes les pièces
  getAllPieces(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  // Récupérer une pièce par ID
  getPieceById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  // Ajouter une nouvelle pièce
  addPiece(piece: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, piece);
  }

  // Mettre à jour une pièce existante
  updatePiece(id: number, piece: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, piece);
  }

  // Supprimer une pièce
  deletePiece(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
