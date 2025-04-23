
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ResponsableSAVServices {
  private apiUrl = 'http://localhost:8888/ResponsableSav';

  constructor(private http: HttpClient) {}
  getAllResponsable(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/responsables`);
  }

  deleteResponsableSAV(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/remove/${id}`);
  }

  updateResponsableSAV(id: number, updatedData: any) {
    return this.http.put(`${this.apiUrl}/update/${id}`, updatedData);
  }

  addResponsableSAV(technicien: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/add`, technicien);
  }
  
  
}

