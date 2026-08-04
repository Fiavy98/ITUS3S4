import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SituationLot {
  NomLot: string;
  NbAkoho: number;
  NbLahy: number;
  NbVavy: number;
  CapaciteMaxAtody: number;
  Achat: number;
  SakafoLany: number;
  Maty: number;
  PoidsMoyen: number;
  PrixVente: number;
  NbAtody: number;
  PrixAtody: number;
  Benefice: number;
}

@Injectable({
  providedIn: 'root'
})
export class SituationService {
  private apiUrl = 'http://localhost:3000/api/situationLot';

  constructor(private http: HttpClient) {}

  getSituationLot(date: string): Observable<SituationLot[]> {
    return this.http.get<SituationLot[]>(`${this.apiUrl}?date=${date}`);
  }
}