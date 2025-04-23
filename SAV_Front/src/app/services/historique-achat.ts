import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root' // Le service est disponible globalement
})
export class HistoriqueAchatService {

  private apiUrl = 'http://localhost:8888/historique'; // Remplace par l'URL de ton API

  constructor(private http: HttpClient) { }

  // Méthode pour obtenir l'historique des achats
  getHistoriqueAchats(idUser: any): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/client/${idUser}`);
  }
  

  //methode pour la dashboard 
  getAchatsParJourSemaine(): Observable<{ [key: string]: number }> {
    return this.http.get<{ [key: string]: number }>(`${this.apiUrl}/par-jour-semaine`);
  }
  
}
