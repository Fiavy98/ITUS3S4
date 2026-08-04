import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, ReplaySubject } from 'rxjs';

export interface PrixRace {
  id: number;
  race_id: number;
  prixU_sakafo_g: number;
  prixV_kg_poulet: number;
  prixV_atody: number;
  prix_achat_poussin: number;
  race_nom?: string;
}

@Injectable({
  providedIn: 'root'
})
export class PrixService {

  private apiUrl = 'http://localhost:3000/api/prixRace';
  private cache = new ReplaySubject<PrixRace[]>(1);
  private loaded = false;

  constructor(private http: HttpClient) {}

  getPrix(): Observable<PrixRace[]> {
    if (!this.loaded) {
      this.http.get<PrixRace[]>(this.apiUrl).subscribe(data => {
        this.loaded = true;
        this.cache.next(data);
      });
    }
    return this.cache.asObservable();
  }

  refresh(): void {
    this.http.get<PrixRace[]>(this.apiUrl).subscribe(data => {
      this.loaded = true;
      this.cache.next(data);
    });
  }

  hasCache(): boolean {
    return this.loaded;
  }

  createPrix(p: Partial<PrixRace>): Observable<PrixRace> {
    return this.http.post<PrixRace>(this.apiUrl, p);
  }



}