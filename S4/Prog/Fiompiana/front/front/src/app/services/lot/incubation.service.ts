import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Incubation {
  id: number;
  lot_source: number;
  lot_source_nom?: string;
  race_id: number;
  race_nom?: string;
  nb_oeufs: number;
  date_debut: string;
  date_eclosion_prevue: string;
  etat: 'incubating' | 'hatched' | 'failed';
  nb_oeufs_hatched: number;
  nb_oeufs_failed: number;
  pct_lahy: number;
  pct_vavy: number;
}

@Injectable({
  providedIn: 'root'
})
export class IncubationService {
  private apiUrl = 'http://localhost:3000/api/incubation';

  constructor(private http: HttpClient) {}

  getIncubations(): Observable<Incubation[]> {
    return this.http.get<Incubation[]>(this.apiUrl);
  }

  createIncubation(data: Partial<Incubation>): Observable<Incubation> {
    return this.http.post<Incubation>(this.apiUrl, data);
  }

  hatchIncubation(id: number, data: Partial<Incubation>): Observable<any> {
    return this.http.post(`${this.apiUrl}/${id}/hatch`, data);
  }
}
