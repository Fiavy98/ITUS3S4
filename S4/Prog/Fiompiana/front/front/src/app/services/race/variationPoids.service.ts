import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, ReplaySubject } from 'rxjs';

export interface VariationPoids {
  id: number;
  semaine: number;
  race_id: number;
  poids_g: number;
  sakafo_g: number;
  race_nom?: string;
}

@Injectable({
  providedIn: 'root'
})
export class VariationPoidsService {
  private apiUrl = 'http://localhost:3000/api/variationPoids';
  private cache = new ReplaySubject<VariationPoids[]>(1);
  private loaded = false;

  constructor(private http: HttpClient) {}

  getVariationPoids(): Observable<VariationPoids[]> {
    if (!this.loaded) {
      this.http.get<VariationPoids[]>(this.apiUrl).subscribe(data => {
        this.loaded = true;
        this.cache.next(data);
      });
    }
    return this.cache.asObservable();
  }

  getVariationByRace(raceId: number): Observable<VariationPoids[]> {
    return this.http.get<VariationPoids[]>(`${this.apiUrl}?race_id=${raceId}`);
  }

  getWeightGain(raceId: number, debut: number, fin: number): Observable<{ startWeight: number; endWeight: number; gain: number }> {
    return this.http.get<{ startWeight: number; endWeight: number; gain: number }>(
      `${this.apiUrl}/poids?race_id=${raceId}&debut=${debut}&fin=${fin}`
    );
  }

  refresh(): void {
    this.http.get<VariationPoids[]>(this.apiUrl).subscribe(data => {
      this.loaded = true;
      this.cache.next(data);
    });
  }

  hasCache(): boolean {
    return this.loaded;
  }

  createVariation(v: Partial<VariationPoids>): Observable<VariationPoids> {
    return this.http.post<VariationPoids>(this.apiUrl, v);
  }


}
