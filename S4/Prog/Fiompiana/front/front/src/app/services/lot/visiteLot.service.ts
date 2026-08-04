import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, ReplaySubject } from 'rxjs';

export interface VisiteLot {
  id: number;
  lot_id: number;
  date_visite: string;
  lahy_maty: number;
  vavy_maty: number;
  nb_atody: number;
  simba?: number;           // œufs détruits
  lot_nom?: string;
}

@Injectable({
  providedIn: 'root'
})
export class VisiteLotService {

  private apiUrl = 'http://localhost:3000/api/visiteLot';
  private cache = new ReplaySubject<VisiteLot[]>(1);
  private loaded = false;

  constructor(private http: HttpClient) {}

  getVisites(): Observable<VisiteLot[]> {
    if (!this.loaded) {
      this.http.get<VisiteLot[]>(this.apiUrl).subscribe(data => {
        this.loaded = true;
        this.cache.next(data);
      });
    }
    return this.cache.asObservable();
  }

  refresh(): void {
    this.http.get<VisiteLot[]>(this.apiUrl).subscribe(data => {
      this.loaded = true;
      this.cache.next(data);
    });
  }

  hasCache(): boolean {
    return this.loaded;
  }

  createVisite(v: Partial<VisiteLot>): Observable<VisiteLot> {
    return this.http.post<VisiteLot>(this.apiUrl, v);
  }



}