import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private baseUrl = 'http://localhost:8888/User'; // à adapter

  constructor(private http: HttpClient) {}

  getStats(): Observable<{ totalClients: number, totalTechniciens: number, totalResponsables: number }> {
    return this.http.get<{ totalClients: number, totalTechniciens: number, totalResponsables: number }>(`${this.baseUrl}/stats`);
  }
}
