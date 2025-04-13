import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TechnicienService {
  private apiUrl = 'http://localhost:8888/technicien';

  constructor(private http: HttpClient) {}


  getAllTechniciens(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/techniciens`);
  }


  getTechniciensBySpecialite(specialite: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/specialite?specialite=${specialite}`);
  }
  

  getAllSpecialites(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/specialites`);
  }
  
  
  
}
