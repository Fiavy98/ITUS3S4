import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, ReplaySubject } from 'rxjs';

export interface Race {
  id: number;
  nom: string;
}

@Injectable({
  providedIn: 'root'
})
export class RaceService {
  private apiUrl = 'http://localhost:3000/api/race'; // ton backend Node
  private cache = new ReplaySubject<Race[]>(1);
  private loaded = false;

  constructor(private http: HttpClient) {}

  getRaces(): Observable<Race[]> {
    if (!this.loaded) {
      this.http.get<Race[]>(this.apiUrl).subscribe(data => {
        this.loaded = true;
        this.cache.next(data);
      });
    }
    return this.cache.asObservable();
  }

  refresh(): void {
    this.http.get<Race[]>(this.apiUrl).subscribe(data => {
      this.loaded = true;
      this.cache.next(data);
    });
  }

  hasCache(): boolean {
    return this.loaded;
  }

  createRace(race: Partial<Race>): Observable<Race> {
    return this.http.post<Race>(this.apiUrl, race);
  }


}




