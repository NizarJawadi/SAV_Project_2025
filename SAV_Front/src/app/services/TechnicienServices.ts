import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TechnicienService {
  private apiUrl = 'http://localhost:8888/technicien';

  constructor(private http: HttpClient) {}

  addTechnicien(technicien: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/add`, technicien); // adapte `/add` à ton endpoint réel
  }

  updateTechnicien(technicien: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${technicien.idUser}`, technicien);
  }
  
 
  
  getAllTechniciens(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/techniciens`);
  }


  getTechniciensBySpecialite(specialite: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/specialite?specialite=${specialite}`);
  }
  

  getAllSpecialites(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/specialites`);
  }

  deleteTechnicien(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/remove/${id}`);
  }
  
  //methodes pour dashboard 
  getTechniciensCountBySpecialite(): Observable<{ [key: string]: number }> {
    return this.http.get<{ [key: string]: number }>(`${this.apiUrl}/count-by-specialite`);
  }
  
  
}
