import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, ReplaySubject } from 'rxjs';

export interface Lot {
  id: number;
  nom: string;
  date_entree: string;
  nb_akoho_initial: number;
  nb_lahy: number;
  nb_vavy: number;
  race_id: number;
  age_semaine: number;
  race_nom?: string; // joined name
}

@Injectable({
  providedIn: 'root'
})
export class LotService {

  private apiUrl = 'http://localhost:3000/api/lot';

  // cache holds the last loaded list
  private cache = new ReplaySubject<Lot[]>(1);
  private loaded = false;

  constructor(private http: HttpClient) {}

  /** observable of the current list; triggers a fetch on first subscription */
  getLots(): Observable<Lot[]> {
    if (!this.loaded) {
      this.http.get<Lot[]>(this.apiUrl).subscribe(data => {
        this.loaded = true;
        this.cache.next(data);
      });
    }
    return this.cache.asObservable();
  }

  /** force reload from server */
  refresh(): void {
    this.http.get<Lot[]>(this.apiUrl).subscribe(data => {
      this.loaded = true;
      this.cache.next(data);
    });
  }

  hasCache(): boolean {
    return this.loaded;
  }

  createLot(lot: Partial<Lot>): Observable<Lot> {
    return this.http.post<Lot>(this.apiUrl, lot);
  }



}